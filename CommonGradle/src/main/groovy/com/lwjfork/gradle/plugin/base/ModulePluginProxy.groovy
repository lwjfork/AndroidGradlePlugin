package com.lwjfork.gradle.plugin.base

import com.lwjfork.gradle.plugin.proxy.IPluginProxy
import org.gradle.api.Project

/**
 * Created by lwj on 2020-01-02.
 *  lwjfork@gmail.com
 */
abstract class ModulePluginProxy extends IPluginProxy {

// 构建
    boolean isAssemble = false
    //默认是app，assembleRelease == app:assembleRelease
    String assembleModule = "app"
    String moduleName = ""
    // module name
    ArrayList<String> modules = new ArrayList<>()


    @Override
    def applyProject(Project project) {
        initRunConfiguration(project)
    }

    def initRunConfiguration(Project project) {
        moduleName = project.path.replace(":", "")
        List<String> taskNames = project.gradle.startParameter.taskNames
        initTaskInfo(taskNames)
        if (isAssemble) { // 构建，获取构建的 module
            if (modules.size() > 0 && modules.get(0) != null) {
                assembleModule = modules.get(0)
            }
        }
    }
// 根据 task 判断，在某些task下才添加依赖
    def initTaskInfo(List<String> taskNames) {
        for (String taskName : taskNames) {
            if (taskName.toUpperCase().contains("ASSEMBLE")  // assemble+buildType
                    || taskName.toUpperCase().contains("AR")  // assembleRelease
                    || taskName.toUpperCase().contains("ASR") //  assembleRelease
                    || taskName.toUpperCase().contains("ASD") //  assembleDebug
                    || taskName.toUpperCase().contains("TINKER") // tinker....
                    || taskName.toUpperCase().contains("INSTALL") // install before  assemble
                    || taskName.toUpperCase().contains("RESGUARD")) { // 资源压缩
                isAssemble = true
                String[] array = taskName.split(":")
                if (array.size() > 1) {
                    modules.add(array[array.length - 2])
                } else {
                    modules.add("app")
                }
                break
            }
        }
    }

}
