package com.codelibs.systrace.transform

import com.android.build.api.transform.*
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformTask
import com.codelibs.systrace.Log
import com.codelibs.systrace.Util

import com.codelibs.systrace.MethodCollector
import com.codelibs.systrace.MethodTracer
import com.codelibs.systrace.TraceBuildConfig
import com.codelibs.systrace.item.TraceMethod
import com.codelibs.systrace.retrace.MappingReader
import com.codelibs.systrace.retrace.MappingCollector
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionGraph
import org.gradle.api.execution.TaskExecutionGraphListener

import java.lang.reflect.Field

/**
 * 在.class文件转换成.dex文件的流程中会执行这个Task，
 * Transform是在.class文件转换成.dex文件期间，把输入的.class文件（包括第三方库的.class）转变成目标字节码文件的一套API，
 * 转换的逻辑定义在Transform的transform()方法中。
 */
public class SystemTraceTransform extends BaseProxyTransform {

    private Transform origTransform
    private Project project
    private def variant

    SystemTraceTransform(Project project, def variant, Transform origTransform) {
        super(origTransform)
        this.origTransform = origTransform
        this.variant = variant
        this.project = project
    }

    /**
     * 注入
     *
     * @param project
     * @param variant
     */
    public static void inject(Project project, def variant) {
        // hackTransformTaskName的值为transformClassesWithDexFor[Variant]，(Variant取值为Debug和Release)
        String hackTransformTaskName = getTransformTaskName("", "", variant.name)

        // hackTransformTaskNameForWrapper的值为transformClassesWithDexBuilderFor[Debug]
        String hackTransformTaskNameForWrapper = getTransformTaskName("", "Builder", variant.name)

        project.logger.info("prepare inject dex transform :" + hackTransformTaskName + " hackTransformTaskNameForWrapper:" + hackTransformTaskNameForWrapper)

        /** 1.定制化的注册Transform的方式（目的是选择合适的时机注入Transform）*/
        project.getGradle().getTaskGraph().addTaskExecutionGraphListener(new TaskExecutionGraphListener() {
            @Override
            public void graphPopulated(TaskExecutionGraph taskGraph) {
                // for循环是在TransformTask和任务图（TaskGraph）创建完成了以后回调中执行的
                for (Task task : taskGraph.getAllTasks()) {
                    // 通过taskName找到TransformTask
                    // 条件为：名称为transformClassesWithDexFor[Variant]或transformClassesWithDexBuilderFor[Variant]的任务，
                    // 且该任务的transform字段类型不是SystemTraceTransform
                    if ((task.name.equalsIgnoreCase(hackTransformTaskName) || task.name.equalsIgnoreCase(hackTransformTaskNameForWrapper))
                            && !(((TransformTask) task).getTransform() instanceof SystemTraceTransform)) {
                        project.logger.warn("find dex transform. transform class: " + task.transform.getClass() + " . task name: " + task.name)
                        project.logger.info("variant name: " + variant.name)

                        // 利用反射将TransformTask中的transform字段替换为项目中自定义的SystemTraceTransform，完成了注册。
                        Field field = TransformTask.class.getDeclaredField("transform")
                        field.setAccessible(true)
                        field.set(task, new SystemTraceTransform(project, variant, task.transform))
                        project.logger.warn("transform class after hook: " + task.transform.getClass())
                        break
                    }
                }
            }
        })

        /**
         // 2.等同于常规注册方法，如下
         // app工程->AppPlugin->AppExtension；library工程->LibraryPlugin->LibraryExtension
         AppExtension appExtension = project.extensions.findByType(AppExtension.class)
         // 常规的注册Transform的方式
         appExtension.registerTransform(new SystemTraceTransform(project, variant, task.transform))
         */
    }

    @Override
    public String getName() {
        return "SystemTraceTransform"
    }

    /**
     * Transform的执行主函数
     *
     * @param transformInvocation
     * @throws TransformException* @throws InterruptedException* @throws IOException
     */
    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        long start = System.currentTimeMillis()
        // 是否增量构建
        final boolean isIncremental = transformInvocation.isIncremental() && this.isIncremental()
        /** 1.遍历以某一扩展名结尾的文件：遍历所有.class结尾的文件 */
        final File rootOutput = new File(project.systrace.output, "classes/${getName()}/")
        if (!rootOutput.exists()) {
            rootOutput.mkdirs()
        }
        final TraceBuildConfig traceConfig = initConfig()
        Log.i("Systrace." + getName(), "[transform] isIncremental:%s rootOutput:%s", isIncremental, rootOutput.getAbsolutePath())
        final MappingCollector mappingCollector = new MappingCollector()
        File mappingFile = new File(traceConfig.getMappingPath())
        if (mappingFile.exists() && mappingFile.isFile()) {
            MappingReader mappingReader = new MappingReader(mappingFile)
            mappingReader.read(mappingCollector)
        }

        Map<File, File> scrInputMap = new HashMap<>()
        Map<File, File> jarInputMap = new HashMap<>()

        /** 2.Transform 的 inputs 有两种类型，一种是目录，一种是 jar 包，要分开遍历 */
        transformInvocation.inputs.each { TransformInput input ->

            /** 3.遍历目录：目录一般指的是项目源码部分 */
            input.directoryInputs.each { DirectoryInput dirInput ->
                /** 3.1 收集并识别目录(class文件夹)，存放到一个HashMap对象中 */
                collectAndIdentifyDir(scrInputMap, dirInput, rootOutput, isIncremental)
            }

            /** 4.遍历 jar：jar是项目依赖的三方jar*/
            input.jarInputs.each { JarInput jarInput ->
                if (jarInput.getStatus() != Status.REMOVED) {
                    /** 4.1 收集并识别jar，存放到一个HashMap对象中 */
                    collectAndIdentifyJar(jarInputMap, scrInputMap, jarInput, rootOutput, isIncremental)
                }
            }
        }

        /**
         * 5.遍历全部目录/jar以及每个目录/jar下的所有.class文件，并收集出所有的方法存在collectedMethodMap中
         */
        MethodCollector methodCollector = new MethodCollector(traceConfig, mappingCollector)
        HashMap<String, TraceMethod> collectedMethodMap = methodCollector.collect(scrInputMap.keySet().toList(), jarInputMap.keySet().toList())
        /**
         * key = methodName ：android.support.v4.app.DialogFragment.onCreate.(Landroid.os.Bundle;)V
         * value = className：android.support.v4.app.DialogFragment
         */
        for(Map.Entry<String, TraceMethod> entry : collectedMethodMap.entrySet()) {
            System.out.println("key = methodName :" + entry.getKey())
            System.out.println("value = className：" + entry.getValue().className)
        }

        MethodTracer methodTracer = new MethodTracer(traceConfig, collectedMethodMap, methodCollector.getCollectedClassExtendMap())
        methodTracer.trace(scrInputMap, jarInputMap)

        origTransform.transform(transformInvocation)

        Log.i("Systrace." + getName(), "[transform] cost time: %dms", System.currentTimeMillis() - start)
    }

    /**
     * 3.1 收集并识别目录，存放到一个HashMap对象中
     *
     * @param dirInputMap
     * @param input
     * @param rootOutput
     * @param isIncremental
     */
    private void collectAndIdentifyDir(Map<File, File> dirInputMap, DirectoryInput input, File rootOutput, boolean isIncremental) {
        final File dirInput = input.file
        final File dirOutput = new File(rootOutput, input.file.getName())
        if (!dirOutput.exists()) {
            dirOutput.mkdirs()
        }
        if (isIncremental) {
            if (!dirInput.exists()) {
                dirOutput.deleteDir()
            } else {
                final Map<File, Status> obfuscatedChangedFiles = new HashMap<>()
                final String rootInputFullPath = dirInput.getAbsolutePath()
                final String rootOutputFullPath = dirOutput.getAbsolutePath()
                input.changedFiles.each { Map.Entry<File, Status> entry ->
                    final File changedFileInput = entry.getKey()
                    final String changedFileInputFullPath = changedFileInput.getAbsolutePath()
                    final File changedFileOutput = new File(changedFileInputFullPath.replace(rootInputFullPath, rootOutputFullPath))
                    final Status status = entry.getValue()
                    switch (status) {
                        case Status.NOTCHANGED:
                            break
                        case Status.ADDED:
                        case Status.CHANGED:
                            dirInputMap.put(changedFileInput, changedFileOutput)
                            break
                        case Status.REMOVED:
                            changedFileOutput.delete()
                            break
                    }
                    obfuscatedChangedFiles.put(changedFileOutput, status)
                }
                replaceChangedFile(input, obfuscatedChangedFiles)
            }
        } else {
            /**
             * 3.2 将准备修改的输入输出class文件夹放到一个HashMap对象中
             *
             * key为：输入的class文件夹，如：dirInput：D:\acto_track\CodeLibs-master\app\build\intermediates\javac\debugFlavorDebug\compileDebugFlavorDebugJavaWithJavac\classes
             * value为：输出class文件夹，如: dirOutput：D:\acto_track\CodeLibs-master\app\build\systrace_output\classes\SystemTraceTransform\classes
             */
            dirInputMap.put(dirInput, dirOutput)
            System.out.println(String.format("[INFO][%s]%s", "dirInput：" + dirInput.toPath() , "  dirOutput：" + dirOutput.toPath()))
        }

        /** 3.3 替换修改文件夹 */
        replaceFile(input, dirOutput)
    }

    /**
     * 4.1 收集并识别jar，存放到一个HashMap对象中
     *
     * @param jarInputMaps
     * @param dirInputMaps
     * @param input
     * @param rootOutput
     * @param isIncremental
     */
    private void collectAndIdentifyJar(Map<File, File> jarInputMaps, Map<File, File> dirInputMaps, JarInput input, File rootOutput, boolean isIncremental) {
        final File jarInput = input.file
        final File jarOutput = new File(rootOutput, getUniqueJarName(jarInput))
        if (Util.isRealZipOrJar(jarInput)) {
            switch (input.status) {
                case Status.NOTCHANGED:
                    if (isIncremental) {
                        break
                    }
                case Status.ADDED:
                case Status.CHANGED:
                    /**
                     * 4.2 将准备修改的输入输出jar文件夹放到一个HashMap对象中
                     *
                     * key为：输入的jar文件，如：jarInput：D:\acto_track\CodeLibs-master\baselib\build\intermediates\intermediate-jars\debug\classes.jar
                     * value为：输出jar文件，如: jarOutput：D:\acto_track\CodeLibs-master\app\build\systrace_output\classes\SystemTraceTransform\classes_7b714b0cc2b8cacee50b9ce6cf548d3ee9a5b0f2.jar
                     */
                    jarInputMaps.put(jarInput, jarOutput)
                    System.out.println(String.format("[INFO][%s]%s", "jarInput：" + jarInput.toPath() , "  jarOutput：" + jarOutput.toPath()))
                    break
                case Status.REMOVED:
                    break
            }
        } else {
            // Special case for WeChat AutoDex. Its rootInput jar file is actually
            // a txt file contains path list.
            BufferedReader br = null
            BufferedWriter bw = null
            try {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(jarInput)))
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(jarOutput)))
                String realJarInputFullPath
                while ((realJarInputFullPath = br.readLine()) != null) {
                    // src jar.
                    final File realJarInput = new File(realJarInputFullPath)
                    // dest jar, moved to extraguard intermediate output dir.
                    final File realJarOutput = new File(rootOutput, getUniqueJarName(realJarInput))

                    if (realJarInput.exists() && Util.isRealZipOrJar(realJarInput)) {
                        jarInputMaps.put(realJarInput, realJarOutput)
                    } else {
                        realJarOutput.delete()
                        if (realJarInput.exists() && realJarInput.isDirectory()) {
                            realJarOutput = new File(rootOutput, realJarInput.getName())
                            if (!realJarOutput.exists()) {
                                realJarOutput.mkdirs()
                            }
                            dirInputMaps.put(realJarInput, realJarOutput)
                        }

                    }
                    // write real output full path to the fake jar at rootOutput.
                    final String realJarOutputFullPath = realJarOutput.getAbsolutePath()
                    bw.writeLine(realJarOutputFullPath)
                }
            } catch (FileNotFoundException e) {
                Log.e("Systrace." + getName(), "FileNotFoundException:%s", e.toString())
            } finally {
                Util.closeQuietly(br)
                Util.closeQuietly(bw)
            }
            jarInput.delete() // delete raw inputList
        }

        /** 4.3 替换修改文件夹 */
        replaceFile(input, jarOutput)
    }


    static private String getTransformTaskName(String customDexTransformName, String wrappSuffix, String buildTypeSuffix) {
        if (customDexTransformName != null && customDexTransformName.length() > 0) {
            return customDexTransformName + "For${buildTypeSuffix}"
        }
        return "transformClassesWithDex${wrappSuffix}For${buildTypeSuffix}"
    }

    /**
     * 初始化配置
     *
     * @return
     */
    private TraceBuildConfig initConfig() {
        def configuration = project.systrace
        def variantName = variant.name.capitalize()
        def mappingFilePath = ""
        if (variant.getBuildType().isMinifyEnabled()) {
            mappingFilePath = variant.mappingFile.getAbsolutePath()
        }
        TraceBuildConfig traceConfig = new TraceBuildConfig.Builder()
                .setPackageName(variant.applicationId)
                .setBaseMethodMap(configuration.baseMethodMapFile)
                .setMethodMapDir(configuration.output + "/${variantName}.methodmap")
                .setIgnoreMethodMapDir(configuration.output + "/${variantName}.ignoremethodmap")
                .setBlackListFile(configuration.blackListFile)
                .setMappingPath(mappingFilePath)
                .build()
        project.logger.info("TraceConfig: " + traceConfig.toString())
        return traceConfig
    }
}

