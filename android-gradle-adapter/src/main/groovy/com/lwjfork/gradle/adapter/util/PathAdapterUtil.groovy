package com.lwjfork.gradle.adapter.util


import com.lwjfork.gradle.adapter.model.VariantInfoModel
import com.lwjfork.gradle.utils.PathUtil
import org.gradle.api.Project

/**
 * Created by lwj on 2020/8/19.
 *  lwjfork@gmail.com
 */
class PathAdapterUtil {
    /**
     *
     * @param project
     * @param dir
     * @param variantInfoModel
     * @return // intermediates/${dir}/${buildType}/${productFlavor}*
     *
     */
    static def getIntermediatesSubDir(Project project, String dir, VariantInfoModel variantInfoModel) {
        def intermediates = PathUtil.getIntermediatesDir(project)
        return "$intermediates$dir${File.separator}${getVariantDir(variantInfoModel)}"
    }

    /**
     *
     * @param project
     * @param variantInfoModel
     * @return build/outs/apk/{buildType}/{productFlavor}
     */
    static def getBuildApkOutSubDir(Project project, VariantInfoModel variantInfoModel) {
        def apkOutDir = PathUtil.getBuildApkOutDir(project)
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
