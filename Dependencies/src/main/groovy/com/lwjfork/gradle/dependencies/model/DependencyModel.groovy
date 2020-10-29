package com.lwjfork.gradle.dependencies.model

import org.gradle.api.artifacts.Dependency

/**
 * Created by lwj on 2020-01-07.
 *  lwjfork@gmail.com
 */
class DependencyModel {
    /**
     *  configurationName The name of the configuration
     *
     *   依赖方式
     */
    String configurationName
    /**
     *  依赖库
     */
    Dependency dependency
    /**
     *  添加依赖时执行的闭包
     */
    Closure configureClosure
}
