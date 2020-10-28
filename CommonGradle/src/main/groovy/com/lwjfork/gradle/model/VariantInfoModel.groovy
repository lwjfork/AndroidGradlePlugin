package com.lwjfork.gradle.model

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
