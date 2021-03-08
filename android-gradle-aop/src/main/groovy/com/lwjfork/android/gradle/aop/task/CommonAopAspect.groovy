package com.lwjfork.android.gradle.aop.task

import com.android.build.api.transform.Context
import com.lwjfork.android.gradle.aop.transform.CommonAnalyzer
import com.lwjfork.gradle.adapter.model.VariantInfoModel
import org.gradle.api.DefaultTask

abstract class CommonAopAspect extends DefaultTask {

    VariantInfoModel variantInfoModel
    def appExtension
    def appPlugin
    CommonAnalyzer analyzer
    Context context


    abstract void initAnalyzer()

    boolean  isNeedAspect(){
        return  true
    }
}
