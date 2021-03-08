package com.lwjfork.android.gradle.aop

import com.lwjfork.android.gradle.aop.proxy.AopProxyPlugin
import com.lwjfork.gradle.adapter.plugin.base.BasePlugin
import com.lwjfork.gradle.utils.Logger
import org.gradle.api.Project

class AopPlugin extends BasePlugin {

    @Override
    void apply(Project project) {
        Logger.project = project
        super.apply(project)
    }

    @Override
    def initPluginProxy() {
        pluginProxies.add(new AopProxyPlugin())
    }
}
