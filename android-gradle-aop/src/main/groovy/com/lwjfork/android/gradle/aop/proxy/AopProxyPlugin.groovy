package com.lwjfork.android.gradle.aop.proxy

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.lwjfork.android.gradle.aop.transform.CommonAopTransform
import com.lwjfork.gradle.adapter.plugin.proxy.IPluginProxy
import org.gradle.api.Project

class AopProxyPlugin extends IPluginProxy {

    @Override
    def applyApp(AppPlugin plugin, AppExtension extension, Project project) {
        CommonAopTransform aopTransform = new CommonAopTransform(project, plugin, extension)
        extension.registerTransform(aopTransform)
    }

}
