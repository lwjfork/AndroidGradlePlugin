package com.lwjfork.gradle.adapter.manifest

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariantOutput
import com.lwjfork.gradle.adapter.util.TaskAdapterUtil
import org.gradle.api.Project

abstract class AppManifestReader extends BaseReadManifest {


    def readManifest(AppPlugin plugin, AppExtension extension, Project project) {
        extension.applicationVariants.all { ApplicationVariant variant ->
            variant.outputs.each { BaseVariantOutput output ->
                TaskAdapterUtil.getProcessManifestTask(output)?.doLast {
                    it.outputs.files.each { File file ->
                        readMultipleManifest(file)
                    }
                }
            }
        }
    }
}
