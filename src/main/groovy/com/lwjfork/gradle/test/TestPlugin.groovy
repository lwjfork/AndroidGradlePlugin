package com.lwjfork.gradle.test

import com.lwjfork.gradle.adapter.plugin.base.BasePlugin
import com.lwjfork.gradle.dependencies.DependenciesConfigProxyPlugin
import com.lwjfork.gradle.inject.InjectViewProxyPlugin
import com.lwjfork.gradle.manifest.UpdateModuleManifestProxyPlugin
import com.lwjfork.gradle.module.ModuleConfigProxyPlugin
import com.lwjfork.gradle.runnable.RunAbleProxyPlugin
import com.lwjfork.gradle.utils.Utils
import org.gradle.api.Project

class TestPlugin  extends BasePlugin {

 @Override
 void apply(Project project) {
  super.apply(project)
 }

 @Override
 Object initPluginProxy() {
  ModuleConfigProxyPlugin configProxyPlugin = new ModuleConfigProxyPlugin()
  DependenciesConfigProxyPlugin dependenciesConfigProxyPlugin = new DependenciesConfigProxyPlugin()
  RunAbleProxyPlugin runAbleProxyPlugin = new RunAbleProxyPlugin() {
   @Override
   def checkRunAble(Project project) {
    super.checkRunAble(project)
    dependenciesConfigProxyPlugin.runAsApp(isRunAble)
    configProxyPlugin.runAsApp(isRunAble)
//    Utils.printLine("isRunAble =" + isRunAble + ', moduleName ='+ currentModule + ' assembleModule=' + assembleModule)
   }
  }
  pluginProxies.add(runAbleProxyPlugin)
  pluginProxies.add(configProxyPlugin)
  pluginProxies.add(dependenciesConfigProxyPlugin)
  pluginProxies.add(new InjectViewProxyPlugin())
  pluginProxies.add(new UpdateModuleManifestProxyPlugin())
  return null
 }
}
