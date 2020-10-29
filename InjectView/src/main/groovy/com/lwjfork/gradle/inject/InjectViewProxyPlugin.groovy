package com.lwjfork.gradle.inject

import com.android.build.gradle.*
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.api.BaseVariantOutput
import com.android.build.gradle.internal.res.GenerateLibraryRFileTask
import com.android.build.gradle.internal.res.LinkApplicationAndroidResourcesTask
import com.android.builder.model.AndroidProject
import com.lwjfork.gradle.inject.task.InjectViewTask
import com.lwjfork.gradle.plugin.proxy.IPluginProxy
import com.lwjfork.gradle.utils.CompatUtil
import org.gradle.api.Project
import org.gradle.api.internal.DefaultDomainObjectSet

import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by lwj on 2020-01-07.
 *  lwjfork@gmail.com
 */
class InjectViewProxyPlugin extends IPluginProxy {

    @Override
    def applyApp(AppPlugin plugin, AppExtension extension, Project project) {
        generatorRFile(project, extension.applicationVariants)
    }

    @Override
    def applyModule(LibraryPlugin plugin, LibraryExtension extension, Project project) {
        generatorRFile(project, extension.libraryVariants)
    }

    @Override
    def applyFeature(FeaturePlugin plugin, FeatureExtension extension, Project project) {
        generatorRFile(project, extension.featureVariants)
        generatorRFile(project, extension.libraryVariants)
    }

    /**
     * 生成 R2 文件
     *   最终的R2 文件会生成在  构建目录下的  /build/generated/source/r2/R2.java
     * @param project
     * @param variants
     * @return
     */
    def generatorRFile(Project project, DefaultDomainObjectSet<BaseVariant> variants) {
        //
        def generatedDir = project.buildDir.absolutePath + File.separator + AndroidProject.FD_GENERATED + File.separator + "source" + File.separator + "r2" + File.separator
        def fileDir = new File(generatedDir)
        if (!fileDir.exists()) {
            fileDir.mkdirs()
        }
        variants.all { BaseVariant variant ->
            def dirName = variant.dirName
            def outPutDir = new File(generatedDir + dirName)
            if (!outPutDir.exists()) {
                outPutDir.mkdirs()
            }
            def packageName = CompatUtil.getPackageName(variant)
            variant.outputs.all { BaseVariantOutput output ->
                def once = new AtomicBoolean()
                def androidResources = CompatUtil.getProcessResourcesTask(output)
                if (once.compareAndSet(false, true)) {
                    def textSymbolOutputFile = project.files({
                        if (androidResources instanceof GenerateLibraryRFileTask) {
                            androidResources.textSymbolOutputFile
                        } else if (androidResources instanceof LinkApplicationAndroidResourcesTask) {
                            androidResources.textSymbolOutputFile
                        } else {
                            throw new RuntimeException(
                                    "Minimum supported Android Gradle Plugin is 3.1.0")
                        }
                    }).builtBy(androidResources)
                    def taskName = "generate${variant.name.capitalize()}_R2"
                    project.tasks.create(taskName, InjectViewTask.class) { it ->
                        it.outPutDir = outPutDir
                        it.packageName = packageName
                        it.className = "R2"
                        it.rFile = textSymbolOutputFile.singleFile
                        variant.registerJavaGeneratingTask(it, outPutDir)
                    }.dependsOn(androidResources)
                }


            }
        }
    }
}