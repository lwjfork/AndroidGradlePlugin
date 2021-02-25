package com.lwjfork.gradle.adapter.manifest

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.BaseVariantOutput
import com.android.build.gradle.api.LibraryVariant
import com.lwjfork.gradle.adapter.util.TaskAdapterUtil
import org.gradle.api.Project

abstract class LibraryManifestReader extends BaseReadManifest {

    def readManifest(LibraryPlugin plugin, LibraryExtension extension, Project project) {
        project.afterEvaluate {
            LibraryExtension libraryExtension = project.extensions.getByType(LibraryExtension)
            libraryExtension.libraryVariants.all { LibraryVariant variant ->
                variant.outputs.each { BaseVariantOutput output ->
                    TaskAdapterUtil.getProcessManifestTask(output).doLast {
                        it.outputs.files.each { File file ->
                            readMultipleManifest(file)
                        }
                    }
                }
            }
        }
    }
}