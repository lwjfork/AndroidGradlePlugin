package com.lwjfork.gradle.utils

import org.gradle.api.Project

/**
 * Created by lwj on 2020/8/19.
 *  lwjfork@gmail.com
 */
class DependencyUtil {

    // 构造 依赖方式  {implementation debugImplementation  releaseImplementation}
    static def getDependencyMode(Project project, String buildType) {
        def dependencyMode = getDependencyModePrefix(project)
        if (!buildType || buildType.length() == 0) {
            return dependencyMode
        }
        return buildType + dependencyMode.capitalize()
    }

    static def getDependencyModePrefix(Project project) {
        String[] arrayVersion = project.gradle.gradleVersion.split("\\.")
        String version = new StringBuilder(arrayVersion[0] + "." + arrayVersion[1]).toString()
        float simpleVersion = version as float
        return simpleVersion >= 4.1F ? 'implementation' : 'compile'
    }
}
