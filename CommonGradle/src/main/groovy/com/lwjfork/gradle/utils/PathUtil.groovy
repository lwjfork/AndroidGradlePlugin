package com.lwjfork.gradle.utils

import com.android.builder.model.AndroidProject
import com.lwjfork.gradle.model.VariantInfoModel
import org.gradle.api.Project

/**
 * Created by lwj on 2020-01-07.
 *  lwjfork@gmail.com
 */
class PathUtil {


    static def getBuildDir(Project project) {
        project.buildDir.absolutePath
    }

    static def getGeneratedDir(Project project) {
        getBuildDir(project) + File.separator + AndroidProject.FD_GENERATED
    }


    static def getOutPutDir(Project project) {
        getBuildDir(project) + File.separator + AndroidProject.FD_OUTPUTS
    }


    static def getIntermediatesDir(Project project) {
        return project.buildDir.absolutePath + File.separator +
                AndroidProject.FD_INTERMEDIATES + File.separator
    }

    static def getBuildApkOutDir(Project project) {
        return project.buildDir.absolutePath + File.separator +
                AndroidProject.FD_OUTPUTS + File.separator + "apk"
    }

    /**
     *
     * @param project
     * @param dir
     * @param variantInfoModel
     * @return // intermediates/${dir}/${buildType}/${productFlavor}*
     *
     */
    static def getIntermediatesSubDir(Project project, String dir, VariantInfoModel variantInfoModel) {
        def intermediates = getIntermediatesDir(project)

        return "$intermediates$dir${File.separator}${getVariantDir(variantInfoModel)}"
    }

    /**
     *
     * @param project
     * @param variantInfoModel
     * @return build/outs/apk/{buildType}/{productFlavor}
     */
    static def getBuildApkOutSubDir(Project project, VariantInfoModel variantInfoModel) {
        def apkOutDir = getBuildApkOutDir(project)
        return "$apkOutDir${File.separator}${getVariantDir(variantInfoModel)}"
    }


    static def getVariantDir(VariantInfoModel variantInfoModel) {
        String buildType = variantInfoModel.buildType
        String flavorName = variantInfoModel.flavorName
        if (flavorName == null || flavorName.length() == 0) {
            return buildType
        } else {
            return flavorName + File.separator + buildType
        }
    }

}
