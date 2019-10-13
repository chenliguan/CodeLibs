package com.codelibs.systrace

import com.codelibs.systrace.extension.SystraceExtension
import com.codelibs.systrace.transform.SystemTraceTransform
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 在com.codelibs.systrace-plugin.properties中配置的入口
 */
class SystracePlugin implements Plugin<Project> {
    private static final String TAG = "SystracePlugin"

    @Override
    void apply(Project project) {
        project.extensions.create("systrace", SystraceExtension)

        if (!project.plugins.hasPlugin('com.android.application')) {
            throw new GradleException('Systrace Plugin, Android Application plugin required')
        }

        // project的gradle配置执行完成后回调
        project.afterEvaluate {
            def android = project.extensions.android
            def configuration = project.systrace

            // 遍历输出的所有variant变体（每一个variant代表一个应用的不同版本，对应productFlavors下的渠道）
            android.applicationVariants.all { variant ->

                String output = configuration.output
                if (Util.isNullOrNil(output)) {
                    configuration.output = project.getBuildDir().getAbsolutePath() + File.separator + "systrace_output"
                    Log.i(TAG, "set Systrace output file to " + configuration.output)
                }

                Log.i(TAG, "Trace enable is %s", configuration.enable)
                if (configuration.enable) {

                    // 调用SystemTraceTransform的静态方法inject()
                    SystemTraceTransform.inject(project, variant)
                }
            }
        }
    }
}
