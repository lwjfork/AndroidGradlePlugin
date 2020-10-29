package com.lwjfork.gradle.dependencies.utils

import com.lwjfork.gradle.utils.CompatUtil
import org.gradle.api.Project

/**
 * Created by lwj on 2020-01-07.
 *  lwjfork@gmail.com
 */
class DependenciesUtil {

    /**
     *
     * implementationComponent or ${buildTypeName}ImplementationComponent
     * compileComponent or ${buildTypeName}CompileComponent
     *
     *
     * 获取自定义的依赖方式
     * @param project
     * @param buildTypeName
     * @return
     */
    static def dependencyName(Project project, String buildTypeName) {
        return "${dependencySysName(project, buildTypeName)}Component"
    }

    /**
     * implementation or ${buildTypeName}Implementation
     * compile or ${buildTypeName}Compile
     *
     * 获取默认的依赖方式
     * @param project
     * @param buildTypeName
     * @return implementation or compile
     */
    static def dependencySysName(Project project, String buildTypeName) {
        if (buildTypeName == null || buildTypeName.length() == 0) {
            "${CompatUtil.getDependencyModePrefix(project)}"
        } else {
            "$buildTypeName${CompatUtil.getDependencyModePrefix(project).capitalize()}"
        }

    }
}
