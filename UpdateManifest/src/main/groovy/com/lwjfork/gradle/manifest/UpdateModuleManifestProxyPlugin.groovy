package com.lwjfork.gradle.manifest

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.lwjfork.gradle.manifest.executor.MultipleModuleManifest
import com.lwjfork.gradle.plugin.proxy.IPluginProxy
import org.gradle.api.Project

/**
 * Created by lwj on 2020-01-06.
 *  lwjfork@gmail.com
 *  修改  lib module 的 AndroidManifest.xml  文件，防止手机桌面出现多个 launcher icon
 */
class UpdateModuleManifestProxyPlugin extends IPluginProxy {

    @Override
    def applyModule(LibraryPlugin plugin, LibraryExtension extension, Project project) {
        // 更新lib的 AndroidManifest.xml  删除 application name 和 Activity launcher  防止出现多个 app icon
        MultipleModuleManifest manifest = new MultipleModuleManifest()
        manifest.deleteMultipleLaunchActivity(project)
    }
}
