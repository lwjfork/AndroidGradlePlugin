package com.lwjfork.gradle.adapter.util

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.ConfigurableFileCollection

/**
 * Created by lwj on 2020/8/19.
 *  lwjfork@gmail.com
 */
class TaskAdapterUtil {

    private static def compatGetTask(def variant, String... candidates) {
        candidates?.findResult {
            variant.metaClass.respondsTo(variant, it).with {
                if (!it.isEmpty()) return it
            }
        }?.find {
            it.getParameterTypes().length == 0
        }?.invoke(variant)?.with {
            Task.class.isInstance(it) ? it : it?.get()
        }
    }
    /**
     *
     * @param variant
     * @return
     * @see com.android.build.gradle.api.BaseVariant
     */
    public static def getAssembleTask(def variant) { // BaseVariant
        return compatGetTask(variant, "getAssembleProvider", "getAssemble")
    }

    /**
     *
     * @param variant
     * @return
     * @see com.android.build.gradle.api.BaseVariant
     */
    public static def getAidlCompileTask(def variant) {
        return compatGetTask(variant, "getAidlCompileProvider", "getAidlCompile")
    }
    /**
     *
     * @param variant
     * @return
     * @see com.android.build.gradle.api.BaseVariant
     */
    public static def getRenderscriptCompileTask(def variant) {
        return compatGetTask(variant, "getRenderscriptCompileProvider", "getRenderscriptCompile")
    }

    /**
     *
     * @param variant
     * @return
     * @see com.android.build.gradle.api.BaseVariant
     */
    public static def getMergeResourcesTask(def variant) {
        return compatGetTask(variant, "getMergeResourcesProvider", "getMergeResources")
    }

    /**
     *
     * @param variant
     * @return
     * @see com.android.build.gradle.api.BaseVariant
     */
    public static def getGenerateBuildConfigTask(def variant) {
        return compatGetTask(variant, "getGenerateBuildConfigProvider", "getGenerateBuildConfig")
    }

    /**
     * @param variant
     * @return
     * @see com.android.build.gradle.api.BaseVariantOutput
     */
    public static def getProcessManifestTask(def variant) {
        return compatGetTask(variant, "getProcessManifestProvider", "getProcessManifest")
    }
    /**
     * @param variant
     * @return
     * @see com.android.build.gradle.api.BaseVariantOutput
     */
    public static def getProcessResourcesTask(def variant) {
        return compatGetTask(variant, "getProcessResourcesProvider", "getProcessResources")
    }

    /**
     * 获取生成 R.text 文件
     * @param project
     * @param androidResourcesTask
     * @return
     */
    static File getGenerateTextSymbolOutputFile(Project project, Task androidResourcesTask) {
        ConfigurableFileCollection fileCollection = project.files(
                {
                    List<MetaMethod>  metaMethods = androidResourcesTask.metaClass.respondsTo(androidResourcesTask,'getTextSymbolOutputFile')
                    boolean hasMethod =  false
                    if(metaMethods.size()> 0 && metaMethods.find {
                        it.getParameterTypes().length == 0
                    }!=null){
                        hasMethod = true
                    }
                    if(hasMethod){
                        androidResourcesTask.getTextSymbolOutputFile()
                    }else if(androidResourcesTask.metaClass.hasProperty(androidResourcesTask,'textSymbolOutputFile')!=null){
                        androidResourcesTask.textSymbolOutputFile
                    }else {
                        throw new GradleException(
                                "Minimum supported Android Gradle Plugin is 3.1.0")
                    }
                }
        ).builtBy(androidResourcesTask)
        return fileCollection.singleFile
    }
}
