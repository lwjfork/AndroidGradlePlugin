package com.lwjfork.gradle.plugin.proxy

import com.android.build.gradle.*
import org.gradle.api.Project

/**
 * Created by lwj on 2020-01-03.
 *  lwjfork@gmail.com
 */
abstract class IPluginProxy {

    def applyProject(Project project) {
    }

    /**
     * Application
     * @param plugin AppPlugin
     * @param extension AppExtension 扩展信息
     * @param project module project
     */
    def applyApp(AppPlugin plugin, AppExtension extension, Project project) {}

    /**
     * Library
     * @param plugin LibraryPlugin
     * @param extension LibraryExtension 扩展信息
     * @param project module project
     */
    def applyModule(LibraryPlugin plugin, LibraryExtension extension, Project project) {}


    /**
     * Feature
     * @param plugin FeaturePlugin
     * @param extension FeatureExtension 扩展信息
     * @param project module project
     */
    def applyFeature(FeaturePlugin plugin, FeatureExtension extension, Project project) {}

}