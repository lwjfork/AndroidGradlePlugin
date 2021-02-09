package com.lwjfork.gradle.adapter.util

import com.lwjfork.gradle.adapter.model.VariantInfoModel

/**
 * Created by lwj on 2020/8/19.
 *  lwjfork@gmail.com
 */
class VariantUtil {

    /**
     * 获取变体信息
     * @param context
     * @return 返回变体信息
     *  @see com.android.build.api.transform.Context
     *  @see com.android.build.gradle.AppExtension
     */
    static VariantInfoModel getVariationInfo(def context, def appExtension) {
        VariantInfoModel variantInfoModel = new VariantInfoModel()
        // 当前变体的名称
        String variantName = context.getVariantName()
        variantInfoModel.variantName = variantName
        def applicationVariant = appExtension.applicationVariants.find {
            it.name == variantName
        }
        variantInfoModel.buildType = applicationVariant.getBuildType().name
        variantInfoModel.flavorName = applicationVariant.getFlavorName()
        variantInfoModel.applicationId = applicationVariant.applicationId
        variantInfoModel.versionName = applicationVariant.versionName
        variantInfoModel.versionCode = applicationVariant.versionCode
        return variantInfoModel
    }

    /**
     *
     * @param variant
     * @return
     * @see com.android.build.gradle.api.BaseVariant
     */
    static String getPackageName(def variant) {
        def xmlSlurper = new XmlSlurper(false, false)
        ArrayList<File> listFile = new ArrayList<>()
        variant.sourceSets.collect { it ->
            listFile.add(it.manifestFile)
        }
        def result = xmlSlurper.parse(listFile.get(0))
        return result.getProperty("@package").toString()
    }
}
