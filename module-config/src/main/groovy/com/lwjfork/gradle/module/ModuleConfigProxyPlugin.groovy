package com.lwjfork.gradle.module

import com.android.build.gradle.internal.dsl.DefaultConfig
import com.lwjfork.gradle.adapter.plugin.proxy.ModulePluginProxy
import com.lwjfork.gradle.utils.ModuleUtil
import org.gradle.api.Project

/**
 * Created by lwj on 2020-01-07.
 *  lwjfork@gmail.com
 */
class ModuleConfigProxyPlugin extends ModulePluginProxy {

    /**
     * 是否可以以 Application 运行
     */
    boolean runAsApp = false


    String COMPONENT_APPLICATION_ID = "componentApplicationId"
    String COMPONENT_APP_ID = "componentAppId"


    @Override
    def applyProject(Project project) {
        super.applyProject(project)
        attachConfigAppIdMethod()
        project.afterEvaluate {
            configApplicationId(project)
        }
        /**
         * 可以以 Application 并且当前运行的就是该 module
         */
        if (runAsApp && assembleModule == moduleName) {
            ModuleUtil.applyApplication(project)

        } else {
            ModuleUtil.applyLibrary(project)
        }
    }


    /**
     * 是否是以 Android Application 运行
     * @param runAsApp
     * @return
     */
    def runAsApp(boolean runAsApp) {
        this.runAsApp = runAsApp
    }

    /**
     * 为 DefaultConfig 类 添加方法和对象属性
     */
    def attachConfigAppIdMethod() {
        DefaultConfig.metaClass."$COMPONENT_APP_ID" = { String applicationId ->
            delegate.metaClass."$COMPONENT_APPLICATION_ID" = applicationId
        }
    }

    /**
     * 配置 applicationID
     * @param project
     * @return
     */
    def configApplicationId(Project project) {
        if (runAsApp && assembleModule == moduleName) {
            String applicationId = getApplicationId(project)
            if (applicationId == null || applicationId.length() == 0) {

                throw new IllegalArgumentException(" componentAppId 不能为空，如果组件 ${project.name} 可以作为APP单独运行，将以 componentAppId 的值作为 applicationId，\n请在build.gradle的defaultConfig节点中添加内容\n\n" +
                        "defaultConfig{\n" +
                        "   // 指定该app的 applicationId\n" +
                        "   componentAppId \"applicationId\"\n" +
                        "   ..........\n" +
                        "}\n")
            }
            project.android.defaultConfig.applicationId = applicationId
        }
    }

    /**
     * 获取 applicationId
     * @param project
     * @return
     */
    def getApplicationId(Project project) {
        String componentAppId = null
        def defaultConfig = project.android.defaultConfig
        if (defaultConfig.metaClass.hasProperty(defaultConfig, COMPONENT_APPLICATION_ID)) {
            componentAppId = project.android.defaultConfig."$COMPONENT_APPLICATION_ID"
        }
        componentAppId
    }


}
