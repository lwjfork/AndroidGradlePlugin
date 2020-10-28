package com.lwjfork.gradle.utils

import org.gradle.api.Project

/**
 * Created by lwj on 2020-01-06.
 *  lwjfork@gmail.com
 */
class TaskUtil {


    /**
     * 对当前task初始化
     * @param project
     * @return
     */
    static boolean isFullCompile(Project project) {
        List<String> taskNames = project.gradle.startParameter.taskNames
        for (String task : taskNames) {
            if (isFullCompile(task)) {
                return true
            }
        }
        return false
    }

    static boolean isFullCompile(String taskName) {
        if (taskName.toUpperCase().contains("ASSEMBLE")
                || taskName.toUpperCase().contains("AR")
                || taskName.toUpperCase().contains("ASR")
                || taskName.toUpperCase().contains("ASD")
                || taskName.toUpperCase().contains("TINKER")
                || taskName.toUpperCase().contains("INSTALL")
                || taskName.toUpperCase().contains("RESGUARD")) {
            return true
        }
        return false
    }

}
