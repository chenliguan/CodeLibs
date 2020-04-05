/*
 * Tencent is pleased to support the open source community by making wechat-matrix available.
 * Copyright (C) 2018 THL A29 Limited, a Tencent company. All rights reserved.
 * Licensed under the BSD 3-Clause License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codelibs.systrace;

import com.codelibs.systrace.item.TraceMethod;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 插桩工具类：对收集的所有方法的输入/输出位置插入trace方法
 *
 * Created by caichongyang on 2017/6/4.
 * <p>
 * This class hooks all collected methods in oder to trace method in/out.
 * </p>
 */
public class MethodTracer {

    private static final String TAG = "Matrix.MethodTracer";
    private static AtomicInteger traceMethodCount = new AtomicInteger();
    private final TraceBuildConfig mTraceConfig;
    private final HashMap<String, TraceMethod> mCollectedMethodMap;
    private final HashMap<String, String> mCollectedClassExtendMap;

    MethodTracer(TraceBuildConfig config, HashMap<String, TraceMethod> collectedMap, HashMap<String, String> collectedClassExtendMap) {
        this.mTraceConfig = config;
        this.mCollectedClassExtendMap = collectedClassExtendMap;
        this.mCollectedMethodMap = collectedMap;
    }

    /**
     * 插入trace方法
     *
     * @param srcFolderList
     * @param dependencyJarList
     */
    public void trace(Map<File, File> srcFolderList, Map<File, File> dependencyJarList) {
        traceMethodFromSrc(srcFolderList);
        traceMethodFromJar(dependencyJarList);
    }

    /**
     * 对目录的所有方法插入trace方法
     *
     * @param srcMap
     */
    private void traceMethodFromSrc(Map<File, File> srcMap) {
        if (null != srcMap) {
            for (Map.Entry<File, File> entry : srcMap.entrySet()) {
                innerTraceMethodFromSrc(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 对Jar的所有方法插入trace方法
     *
     * @param dependencyMap
     */
    private void traceMethodFromJar(Map<File, File> dependencyMap) {
        if (null != dependencyMap) {
            for (Map.Entry<File, File> entry : dependencyMap.entrySet()) {
                innerTraceMethodFromJar(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 对目录的每个方法插入trace方法
     *
     * @param input
     * @param output
     */
    private void innerTraceMethodFromSrc(File input, File output) {
        ArrayList<File> classFileList = new ArrayList<>();
        if (input.isDirectory()) {
            listClassFiles(classFileList, input);
        } else {
            classFileList.add(input);
        }

        for (File classFile : classFileList) {
            InputStream is = null;
            FileOutputStream os = null;
            try {
                final String changedFileInputFullPath = classFile.getAbsolutePath();
                final File changedFileOutput = new File(changedFileInputFullPath.replace(input.getAbsolutePath(), output.getAbsolutePath()));
                if (!changedFileOutput.exists()) {
                    changedFileOutput.getParentFile().mkdirs();
                }
                changedFileOutput.createNewFile();

                if (mTraceConfig.isNeedTraceClass(classFile.getName())) {
                    is = new FileInputStream(classFile);

                    // 1、修改.class文件
                    // （1）先通过ClassReader解析编译后.class文件的原始字节码并加载类
                    ClassReader classReader = new ClassReader(is);
                    // （2）创建ClassWriter类基于不同的Visitor类进行重新修改编译后的类、属性、方法，生成新的类字节码文件
                    ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                    // （3）ClassVisitor用于访问类成员信息的工具。在visit()和visitMethod()里对类名和方法名进行判断，需要处理在ClassVisitor类中插入字节码
                    ClassVisitor classVisitor = new TraceClassAdapter(Opcodes.ASM5, classWriter);
                    // （4）使用自定义的ClassVisitor访问类并进行修改符合特定条件的方法
                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
                    is.close();

                    // 2、替换
                    // （1）从classWriter得到class修改后的byte流，然后通过流的写入覆盖原来的class文件
                    if (output.isDirectory()) {
                        os = new FileOutputStream(changedFileOutput);
                    } else {
                        os = new FileOutputStream(output);
                    }
                    os.write(classWriter.toByteArray());
                    os.close();
                } else {
                    Util.copyFileUsingStream(classFile, changedFileOutput);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                    os.close();
                } catch (Exception e) {
                    // ignore
                }
            }
        }
    }

    private void innerTraceMethodFromJar(File input, File output) {
        ZipOutputStream zipOutputStream = null;
        ZipFile zipFile = null;
        try {
            zipOutputStream = new ZipOutputStream(new FileOutputStream(output));
            zipFile = new ZipFile(input);
            Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
            while (enumeration.hasMoreElements()) {
                ZipEntry zipEntry = enumeration.nextElement();
                String zipEntryName = zipEntry.getName();
                if (mTraceConfig.isNeedTraceClass(zipEntryName)) {
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    ClassReader classReader = new ClassReader(inputStream);
                    ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                    ClassVisitor classVisitor = new TraceClassAdapter(Opcodes.ASM5, classWriter);
                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
                    byte[] data = classWriter.toByteArray();
                    InputStream byteArrayInputStream = new ByteArrayInputStream(data);
                    ZipEntry newZipEntry = new ZipEntry(zipEntryName);
                    Util.addZipEntry(zipOutputStream, newZipEntry, byteArrayInputStream);
                } else {
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    ZipEntry newZipEntry = new ZipEntry(zipEntryName);
                    Util.addZipEntry(zipOutputStream, newZipEntry, inputStream);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "[traceMethodFromJar] err! %s", output.getAbsolutePath());
        } finally {
            try {
                if (zipOutputStream != null) {
                    zipOutputStream.finish();
                    zipOutputStream.flush();
                    zipOutputStream.close();
                }
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (Exception e) {
                Log.e(TAG, "close stream err!");
            }
        }
    }

    private void listClassFiles(ArrayList<File> classFiles, File folder) {
        File[] files = folder.listFiles();
        if (null == files) {
            Log.e(TAG, "[listClassFiles] files is null! %s", folder.getAbsolutePath());
            return;
        }
        for (File file : files) {
            if (file == null) {
                continue;
            }
            if (file.isDirectory()) {
                listClassFiles(classFiles, file);
            } else {
                if (null != file && file.isFile()) {
                    classFiles.add(file);
                }

            }
        }
    }

    /**
     * 实现了ClassVisitor，负责访问类成员信息
     */
    private class TraceClassAdapter extends ClassVisitor {

        private String className;
        private boolean isABSClass = false;
        private boolean isMethodBeatClass = false;

        TraceClassAdapter(int i, ClassVisitor classVisitor) {
            super(i, classVisitor);
        }

        /**
         * 可以拿到类.class的所有信息，然后对满足条件的类进行过滤（当扫描类时第一个会调用的方法）
         *
         * @param version     jdk版本
         * @param access      类的修饰符：ASM中以ACC_开头，ACC_PUBLIC
         * @param name        类的名称：使用完整的包名+类名表示，a.b.c.MyClass --> a/b/c/MyClass（不需要.class扩展名）
         * @param signature   表示泛型信息：未定义，则为空
         * @param superName   表示当前类所继承的父类
         * @param interfaces  表示类所实现的接口列表
         */
        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces);
            this.className = name;
            if ((access & Opcodes.ACC_ABSTRACT) > 0 || (access & Opcodes.ACC_INTERFACE) > 0) {
                this.isABSClass = true;
            }

            // （1）对类名和方法名进行判断
            if (mTraceConfig.isMethodBeatClass(className, mCollectedClassExtendMap)) {
                isMethodBeatClass = true;
            }
        }

        /**
         * 拿到需要修改的方法的所哟信息，比如：方法名、方法参数描述，然后进行修改操作（当扫描器扫描到类的方法时进行调用）
         *
         * @param access      类的修饰符：ASM中以ACC_开头，ACC_PUBLIC
         * @param name        类的名称：使用完整的包名+类名表示，a.b.c.MyClass --> a/b/c/MyClass（不需要.class扩展名）
         * @param desc        方法签名：代码-->类型，eg：I-->int,D-->Double,String[]-->[Ljava/lang/String;
         * @param signature   表示泛型信息：未定义，则为空
         * @param exceptions  表示抛出的异常
         * @return
         */
        @Override
        public MethodVisitor visitMethod(int access, String name, String desc,
                                         String signature, String[] exceptions) {
            if (isABSClass) {
                return super.visitMethod(access, name, desc, signature, exceptions);
            } else {
                MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);

                // （2）TraceMethodAdapter主要负责访问方法的信息，用来进行具体的方法字节码操作
                return new TraceMethodAdapter(api, methodVisitor, access, name, desc, this.className, isMethodBeatClass);
            }
        }

        /**
         * 遍历类中成员结束
         */
        @Override
        public void visitEnd() {
            super.visitEnd();
        }
    }

    /**
     * 主要负责访问方法的信息，用来进行具体的方法字节码操作
     */
    private class TraceMethodAdapter extends AdviceAdapter {

        private final String methodName;
        private final String name;
        private final String className;
        private final boolean isMethodBeatClass;

        protected TraceMethodAdapter(int api, MethodVisitor mv, int access, String name, String desc, String className,
                                     boolean isMethodBeatClass) {
            super(api, mv, access, name, desc);
            TraceMethod traceMethod = TraceMethod.create(0, access, className, name, desc);
            this.methodName = traceMethod.getMethodName();
            this.isMethodBeatClass = isMethodBeatClass;
            this.className = className;
            this.name = name;
        }

        /**
         * 进入方法时插入字节码
         */
        @Override
        protected void onMethodEnter() {
            TraceMethod traceMethod = mCollectedMethodMap.get(methodName);
            if (traceMethod != null) {
                traceMethodCount.incrementAndGet();
                String sectionName = methodName;
                int length = sectionName.length();
                if (length > TraceBuildConstants.MAX_SECTION_NAME_LEN) {
                    // gonna take out the parameters
                    int parmIndex = sectionName.indexOf('(');
                    sectionName = sectionName.substring(0, parmIndex);
                    // If it's still bigger, cut it
                    length = sectionName.length();
                    if (length > TraceBuildConstants.MAX_SECTION_NAME_LEN) {
                        sectionName = sectionName.substring(length - TraceBuildConstants.MAX_SECTION_NAME_LEN);
                    }
                }
                mv.visitLdcInsn(sectionName);

                /**
                 * 方法：com/systrace/TraceTag void i(String name)
                 * String --> (Ljava/lang/String;)
                 * V -- > void
                 */
                mv.visitMethodInsn(INVOKESTATIC, TraceBuildConstants.MATRIX_TRACE_METHOD_BEAT_CLASS, "i", "(Ljava/lang/String;)V", false);
            }
        }

        /**
         * 退出方法前可以插入字节码
         */
        @Override
        protected void onMethodExit(int opcode) {
            //if (isMethodBeatClass && ("<clinit>").equals(name)) {
            //    StringBuffer stringBuffer = new StringBuffer();
            //
            //    stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            //    mv.visitLdcInsn(stringBuffer.toString());
            //    mv.visitFieldInsn(Opcodes.PUTSTATIC, className, TraceBuildConstants.MATRIX_TRACE_APPLICATION_CREATE_FILED, "Ljava/lang/String;");
            //}

            TraceMethod traceMethod = mCollectedMethodMap.get(methodName);
            if (traceMethod != null) {
                traceMethodCount.incrementAndGet();

                // mv.visitLdcInsn(traceMethod.id);
                /**
                 * 方法：com/systrace/TraceTag void o()
                 * V -- > void
                 */
                mv.visitMethodInsn(INVOKESTATIC, TraceBuildConstants.MATRIX_TRACE_METHOD_BEAT_CLASS, "o", "()V", false);
            }
        }
    }
}
