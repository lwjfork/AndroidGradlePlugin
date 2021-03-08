package com.lwjfork.android.gradle.aop.task

import com.android.build.gradle.AppExtension
import com.lwjfork.android.gradle.aop.analyzer.CommonAnalyzer
import com.lwjfork.gradle.adapter.model.VariantInfoModel
import org.gradle.api.DefaultTask

abstract class CommonAopAspect extends DefaultTask {

    VariantInfoModel variantInfoModel
    AppExtension appExtension
    CommonAnalyzer analyzer


    abstract void initAnalyzer()

    boolean  isNeedAspect(){
        return  true
    }
}
