package com.lwjfork.gradle.adapter.model

/**
 * Created by lwj on 2020-01-05.
 *  lwjfork@gmail.com
 */
class VariantInfoModel {

    String variantName
    String buildType
    String flavorName
    String applicationId
    int versionCode
    String versionName


    @Override
    public String toString() {
        return "VariantInfoModel{" +
                "variantName='" + variantName + '\'' +
                ", buildType='" + buildType + '\'' +
                ", flavorName='" + flavorName + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                '}';
    }
}
