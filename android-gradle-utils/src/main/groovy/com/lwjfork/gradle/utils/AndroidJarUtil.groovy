package com.lwjfork.gradle.utils
import com.android.sdklib.IAndroidTarget
/**
 * 获取 android.jar 的路径
 */
class AndroidJarUtil {
    // android.jar 路径
    private String androidJarPath = null
    private def globalScope
    String findAndroidJarPath() {
        if (androidJarPath != null && androidJarPath.length() > 0) {
            return androidJarPath
        }

        final MetaClass scopeClz = globalScope.metaClass
        if (scopeClz.hasProperty(globalScope, "androidBuilder")) {
            androidJarPath = globalScope.getAndroidBuilder().getTarget().getPath(IAndroidTarget.ANDROID_JAR)
        }
        if (scopeClz.hasProperty(globalScope, "sdkComponents")) {
            androidJarPath = globalScope.sdkComponents.androidJarProvider.get().getAbsolutePath()
        }
        androidJarPath
    }

}
