package com.lwjfork.gradle.utils

import com.android.build.gradle.BaseExtension
import com.android.sdklib.IAndroidTarget
import org.gradle.api.provider.Provider

/**
 * 获取 android.jar 的路径
 */
class AndroidJarUtil {
    // android.jar 路径
    private static String androidJarPath = null


    static String getAndroidJarPath(def appExtension) {
        if (androidJarPath != null && androidJarPath.length() > 0) {
            return androidJarPath
        }
        String androidJarPath = ''
        def globalScope = BaseExtension.metaClass.getProperty(appExtension, 'globalScope')
        final MetaClass scopeClz = globalScope.metaClass
        if (scopeClz.hasProperty(globalScope, "androidBuilder")) {
            androidJarPath = globalScope.getAndroidBuilder().getTarget().getPath(IAndroidTarget.ANDROID_JAR)
        } else if (scopeClz.hasProperty(globalScope, "sdkComponents")) {
            def sdkComponents = globalScope.sdkComponents
            if (sdkComponents instanceof Provider) {
                sdkComponents = sdkComponents.get()
            }
            if (sdkComponents.hasProperty('androidJarProvider')) {
                androidJarPath = sdkComponents.androidJarProvider.get().getAbsolutePath()
            }
        }
        Logger.lw("android.jar is in ${androidJarPath}")
        this.androidJarPath = androidJarPath
        return androidJarPath
    }

}
