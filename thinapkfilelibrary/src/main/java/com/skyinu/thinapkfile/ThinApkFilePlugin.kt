package com.skyinu.thinapkfile

import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.tasks.PackageApplication
import org.gradle.api.DomainObjectSet

class ThinApkFilePlugin : Plugin<Project> {
    companion object {
        const val PACKAGE_APK_TASK_PREFIX = "package"
    }

    override fun apply(project: Project) {
        project.plugins.all {
            when (it) {
                is AppPlugin -> {
                    project.extensions.findByType(AppExtension::class.java)?.run {
                        configThinkApkFileTask(project, applicationVariants)
                    }
                }
            }
        }
    }

    private fun configThinkApkFileTask(project: Project, variants: DomainObjectSet<out BaseVariant>) {
        variants.all { variant ->
            project.task("$PACKAGE_APK_TASK_PREFIX${variant.name}")
                .doLast {
                    ThinkApkFileTask.thinApkFile(it as PackageApplication)
                }
        }
    }
}