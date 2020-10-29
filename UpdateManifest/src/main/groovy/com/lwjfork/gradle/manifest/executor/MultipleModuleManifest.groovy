package com.lwjfork.gradle.manifest.executor

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.BaseVariantOutput
import com.android.build.gradle.api.LibraryVariant
import com.lwjfork.gradle.utils.CompatUtil
import groovy.util.slurpersupport.GPathResult
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.gradle.api.Project

/**
 * Created by lwj on 2020-01-07.
 *  lwjfork@gmail.com
 */
class MultipleModuleManifest {

    // 更新lib的 AndroidManifest.xml  删除 application name 和 Activity launcher  防止出现多个 app icon
    def deleteMultipleLaunchActivity(Project project) {
        project.afterEvaluate {
            LibraryExtension libraryExtension = project.extensions.getByType(LibraryExtension)
            libraryExtension.libraryVariants.all { LibraryVariant variant ->
                variant.outputs.each { BaseVariantOutput output ->
                    CompatUtil.getProcessManifestTask(output).doLast {
                        it.outputs.files.each { File file ->
                            deleteMultipleManifest(file)
                        }
                    }
                }
            }
        }
    }


    def deleteMultipleManifest(File file) {
        if (file == null || !file.exists()) return
        if (file.isDirectory()) {
            file.listFiles().each {
                deleteMultipleManifest(it)
            }
        } else if (file.name.equalsIgnoreCase("AndroidManifest.xml")) {
            deleteMultipleLaunchActivity(file)
        }
    }

    def deleteMultipleLaunchActivity(File file) {
        XmlSlurper xmlSlurper = new XmlSlurper(false, false)
        GPathResult manifest = xmlSlurper.parse(file)
        // 删除 application name
        manifest.application.each {
            it.attributes().remove("android:name")
        }
        // 删除属性，防止出现多个桌面icon
        manifest.application.activity.each { activity ->
            def filter = activity.'intent-filter'.find {
                it.action.@'android:name' == "android.intent.action.MAIN"
            }
            if (filter != null && filter.size) {
                activity.'intent-filter'.each {
                    if (it.action.@'android:name' == "android.intent.action.MAIN") {
                        it.replaceNode {}
                    }
                }
            }
        }

        StreamingMarkupBuilder outputBuilder = new StreamingMarkupBuilder()
        String root = outputBuilder.bind {
            mkp.xmlDeclaration()
            mkp.yield manifest
        }
        String result = XmlUtil.serialize(root)
        file.text = result
    }

}
