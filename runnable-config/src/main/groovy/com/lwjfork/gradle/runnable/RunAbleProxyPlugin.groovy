package com.lwjfork.gradle.runnable

import com.lwjfork.gradle.adapter.plugin.proxy.ModulePluginProxy
import org.gradle.api.Project

class RunAbleProxyPlugin extends ModulePluginProxy {

    String RUN_APP = "runAble"

    boolean isRunAble = false


    @Override
    def applyProject(Project project) {
        checkRunAble(project)
    }

    def checkRunAble(Project project) {
        initRunConfiguration(project)
        // 配置是否可以单独运行
        def runAbleConfig = getRunAbleFromProperty(project)
        if(runAbleConfig){
            if(isAssembleTask){ // 执行打包任务
                // 当前module 就是打包的 module 则可以运行，否则不运行
                isRunAble = currentModule == assembleModule
            }else {
                isRunAble = true
            }
        }else {
            isRunAble = false
        }
    }
    /**
     * 获取 配置的 runAble 属性
     * @param project
     * @return
     */
    def getRunAbleFromProperty(Project project) {
        if (project.hasProperty(RUN_APP)) {
            Boolean.valueOf(project.properties.get(RUN_APP))
        } else {

            throw new RuntimeException("$moduleName 的gradle.properties文件需要配置runAble属性，来指定是否可以以App运行\n" +
                    "在文件 ${project.getProjectDir().name + File.separator + "gradle.properties 中添加如下内容：\n\n "}" +
                    "# 如果可以以Application 运行则，runAble为true，否则为false\n" +
                    " runAble=true" +
                    " \n" +
                    " \n")
        }
    }
}
