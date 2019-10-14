package com.codelibs.systrace.transform

import java.lang.reflect.Field

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Status
import com.android.build.api.transform.Transform
import com.google.common.base.Charsets
import com.google.common.hash.Hashing
import com.codelibs.systrace.ReflectUtil

public abstract class BaseProxyTransform extends Transform {
    protected final Transform origTransform

    public BaseProxyTransform(Transform origTransform) {
        this.origTransform = origTransform
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return origTransform.getInputTypes()
    }

    @Override
    Set<QualifiedContent.Scope> getScopes() {
        return origTransform.getScopes()
    }

    @Override
    boolean isIncremental() {
        return origTransform.isIncremental()
    }

    protected String getUniqueJarName(File jarFile) {
        final String origJarName = jarFile.getName()
        final String hashing = Hashing.sha1().hashString(jarFile.getPath(), Charsets.UTF_16LE).toString()
        final int dotPos = origJarName.lastIndexOf('.')
        if (dotPos < 0) {
            return "${origJarName}_${hashing}"
        } else {
            final String nameWithoutDotExt = origJarName.substring(0, dotPos)
            final String dotExt = origJarName.substring(dotPos)
            return "${nameWithoutDotExt}_${hashing}${dotExt}"
        }
    }

    /**
     * 替换修改文件夹
     *
     * class
     * input：D:\acto_track\CodeLibs-master\app\build\systrace_output\classes\SystemTraceTransform\classes
     * newFile：D:\acto_track\CodeLibs-master\app\build\systrace_output\classes\SystemTraceTransform\classes
     *
     * jar
     * input：D:\acto_track\CodeLibs-master\app\build\systrace_output\classes\SystemTraceTransform\classes_7b714b0cc2b8cacee50b9ce6cf548d3ee9a5b0f2.jar
     * newFile：D:\acto_track\CodeLibs-master\app\build\systrace_output\classes\SystemTraceTransform\classes_7b714b0cc2b8cacee50b9ce6cf548d3ee9a5b0f2.jar
     *
     * @param input
     * @param newFile
     */
    protected void replaceFile(QualifiedContent input, File newFile) {
        final Field fileField = ReflectUtil.getDeclaredFieldRecursive(input.getClass(), 'file')
        fileField.set(input, newFile)
        System.out.println(String.format("[INFO][%s]%s", "input：" + input.getFile() , "  newFile：" + newFile.toPath()))
    }

    protected void replaceChangedFile(DirectoryInput dirInput, Map<File, Status> changedFiles) {
        final Field changedFilesField = ReflectUtil.getDeclaredFieldRecursive(dirInput.getClass(), 'changedFiles')
        changedFilesField.set(dirInput, changedFiles)
    }
}
