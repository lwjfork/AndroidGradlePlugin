package com.lwjfork.gradle.utils

import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.api.BaseVariantOutput
import com.android.build.gradle.internal.res.GenerateLibraryRFileTask
import com.android.build.gradle.internal.res.LinkApplicationAndroidResourcesTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.ConfigurableFileCollection

/**
 * Created by lwj on 2019-09-10.
 * lwjfork@gmail.com
 *
 *  Task 获取 适配
 *
 */
class CompatUtil {

    public static def getAssembleTask(BaseVariant variant) { // BaseVariant
        return compatGetTask(variant, "getAssembleProvider", "getAssemble")
    }


    public static def getAidlCompileTask(BaseVariant variant) {
        return compatGetTask(variant, "getAidlCompileProvider", "getAidlCompile")
    }

    public static def getRenderscriptCompileTask(BaseVariant variant) {
        return compatGetTask(variant, "getRenderscriptCompileProvider", "getRenderscriptCompile")
    }


    public static def getMergeResourcesTask(BaseVariant variant) {
        return compatGetTask(variant, "getMergeResourcesProvider", "getMergeResources")
    }


    public static def getGenerateBuildConfigTask(BaseVariant variant) {
        return compatGetTask(variant, "getGenerateBuildConfigProvider", "getGenerateBuildConfig")
    }


    public static def getProcessManifestTask(BaseVariantOutput variant) {
        return compatGetTask(variant, "getProcessManifestProvider", "getProcessManifest")
    }

    public static def getProcessResourcesTask(BaseVariantOutput variant) {
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
                    if (androidResourcesTask instanceof GenerateLibraryRFileTask) {
                        androidResourcesTask.textSymbolOutputFile
                    } else if (androidResourcesTask instanceof LinkApplicationAndroidResourcesTask) {
                        androidResourcesTask.textSymbolOutputFile
                    } else {
                        throw RuntimeException(
                                "Minimum supported Android Gradle Plugin is 3.1.0")
                    }
                }
        ).builtBy(androidResourcesTask)
        return fileCollection.singleFile
    }


    static File getGenerateTextSymbolOutputFile(BaseVariantOutput output) {
        Task task = getProcessResourcesTask(output)
        return getGenerateTextSymbolOutputFile(task)
    }


    /**
     * 获取包名
     * @param variant 构建变体
     * @return 返回包名
     */
    static String getPackageName(BaseVariant variant) {
        def xmlSlurper = new XmlSlurper(false, false)
        ArrayList<File> listFile = new ArrayList<>()
        variant.sourceSets.collect { it ->
            listFile.add(it.manifestFile)
        }
        def result = xmlSlurper.parse(listFile.get(0))
        return result.getProperty("@package").toString()
    }

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


    // 构造 依赖方式  {implementation debugImplementation  releaseImplementation}
    static def getDependencyMode(Project project, String buildType) {
        def dependencyMode = getDependencyModePrefix(project)
        if (Utils.isEmpty(buildType)) {
            return dependencyMode
        }
        return buildType + dependencyMode.capitalize()
    }

    static def getDependencyModePrefix(Project project) {
        String[] arrayVersion = project.gradle.gradleVersion.split("\\.")
        String version = new StringBuilder(arrayVersion[0] + "." + arrayVersion[1]).toString()
        float simpleVersion = version as float
        return simpleVersion >= 4.1F ? 'implementation' : 'compile'
    }
}
