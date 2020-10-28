package com.lwjfork.gradle.utils


import org.gradle.api.Project

/**
 * Created by lwj on 2020-01-06.
 *  lwjfork@gmail.com
 */
class ModuleUtil {


    static def ANDROID_APPLICATION = "com.android.application"
    static def ANDROID_LIBRARY = "com.android.library"

    //  config application
    static void applyApplication(Project project) {
        Utils.printLine("【 $project.name 】 will run as Android Applicaion")
        project.apply plugin: ANDROID_APPLICATION
    }
    // config library
    static void applyLibrary(Project project) {
        Utils.printLine("【 $project.name 】will run as Android Library")
        project.apply plugin: ANDROID_LIBRARY
    }


}
