package com.lwjfork.gradle.adapter.plugin.base

import com.android.build.gradle.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.lwjfork.gradle.adapter.plugin.proxy.IPluginProxy

/**
 * Created by lwj on 2020-01-03.
 *  lwjfork@gmail.com
 */
abstract class BasePlugin extends IPluginProxy implements Plugin<Project> {


    ArrayList<IPluginProxy> pluginProxies = new ArrayList<>()

    abstract def initPluginProxy()

    @Override
    void apply(Project project) {
        initPluginProxy()
        pluginProxies.each { pluginProxy ->
            pluginProxy.applyProject(project)
        }
        project.plugins.all { plugin ->
            if (plugin instanceof AppPlugin) {
                applyApp((AppPlugin) plugin, (AppExtension) plugin.extension, project)
            } else if (plugin instanceof LibraryPlugin) {
                applyModule((LibraryPlugin) plugin, (LibraryExtension) plugin.extension, project)
            } else if (plugin instanceof FeaturePlugin) {
                applyFeature((FeaturePlugin) plugin, (FeatureExtension) plugin.extension, project)
            }
        }
    }


    @Override
    def applyApp(AppPlugin plugin, AppExtension extension, Project project) {
        pluginProxies.each { pluginProxy ->
            pluginProxy.applyApp(plugin, extension, project)
        }
    }

    @Override
    def applyModule(LibraryPlugin plugin, LibraryExtension extension, Project project) {
        pluginProxies.each { pluginProxy ->
            pluginProxy.applyModule(plugin, extension, project)
        }
    }

    @Override
    def applyFeature(FeaturePlugin plugin, FeatureExtension extension, Project project) {
        pluginProxies.each { pluginProxy ->
            pluginProxy.applyFeature(plugin, extension, project)
        }
    }

}
