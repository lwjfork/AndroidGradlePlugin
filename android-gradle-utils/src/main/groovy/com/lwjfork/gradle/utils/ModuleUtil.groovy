package com.lwjfork.gradle.utils

import org.gradle.api.Project

/**
 * Created by lwj on 2020/8/19.
 *  lwjfork@gmail.com
 */
class ModuleUtil {
    static def ANDROID_APPLICATION = "com.android.application"
    static def ANDROID_LIBRARY = "com.android.library"

    //  config application
    static void applyApplication(Project project) {
        project.apply plugin: ANDROID_APPLICATION
    }
    // config library
    static void applyLibrary(Project project) {
        project.apply plugin: ANDROID_LIBRARY
    }
}
