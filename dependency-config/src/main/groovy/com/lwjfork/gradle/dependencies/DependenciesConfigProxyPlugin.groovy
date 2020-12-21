package com.lwjfork.gradle.dependencies

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BasePlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.lwjfork.gradle.dependencies.model.DependencyModel
import com.lwjfork.gradle.dependencies.utils.DependenciesUtil
import com.lwjfork.gradle.adapter.plugin.proxy.ModulePluginProxy
import com.lwjfork.gradle.utils.Utils
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency

/**
 * Created by lwj on 2020-01-07.
 *  lwjfork@gmail.com
 */
class DependenciesConfigProxyPlugin extends ModulePluginProxy {

    boolean runAsApp = false


    /**
     * 储存的依赖
     *  每个buildType 对应了各自的依赖
     */
    Map<String, ArrayList<DependencyModel>> configDependencies = new HashMap<>()
    ArrayList<String> projectBuildTypes = new ArrayList<>()


    @Override
    def applyApp(AppPlugin plugin, AppExtension extension, Project project) {
        attachMethod(project, plugin)
        applyDependencyHandler(project)
    }

    @Override
    def applyModule(LibraryPlugin plugin, LibraryExtension extension, Project project) {
        attachMethod(project, plugin)
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
     * 添加依赖
     * 使用该方式添加的依赖是 可以执行闭包的
     * @param project
     * @param plugin
     * @return
     */
    def applyDependencyHandler(Project project) {
        if (runAsApp && assembleModule == moduleName) {
            project.afterEvaluate {
                configDependencies.eachWithIndex { Map.Entry<String, ArrayList<DependencyModel>> entry, int i ->
                    entry.value.each {
                        if (it.configureClosure != null) {
                            project.dependencies.add(it.configurationName, it.dependency, it.configureClosure)
                        } else {
                            project.dependencies.add(it.configurationName, it.dependency)
                        }
                    }
                }
            }
        }
    }





    /**
     * 添加依赖方法
     */
    def attachDependencyMethod(Project project, String buildTypeName, ExpandoMetaClass expandoDependencies) {
        String methodName = DependenciesUtil.dependencyName(project, buildTypeName)
        attachDependencyMethod(project, methodName, buildTypeName, expandoDependencies)
    }


    /**
     * 添加依赖方法
     */
    def attachDependencyMethod(Project project, String methodName, String buildTypeName, ExpandoMetaClass expandoDependencies) {

        expandoDependencies."$methodName" = { String name ->
            Dependency dependency = project.dependencies.create(name)
            addDependency(project, buildTypeName, dependency, null)
        }
        expandoDependencies."$methodName" = { Dependency dependency ->
            addDependency(project, buildTypeName, dependency, null)
        }
        expandoDependencies."$methodName" = { Project subProject ->
            Dependency dependency = project.dependencies.project(path: ":${subProject.name}")
            addDependency(project, buildTypeName, dependency, null)
        }


        expandoDependencies."$methodName" = { String name, Closure closure ->
            Dependency dependency = project.dependencies.create(name)
            addDependency(project, buildTypeName, dependency, closure)
        }
        expandoDependencies."$methodName" = { Dependency dependency, Closure closure ->
            addDependency(project, buildTypeName, dependency, closure)
        }
        expandoDependencies."$methodName" = { Project subProject, Closure closure ->
            Dependency dependency = project.dependencies.project(path: ":${subProject.name}")
            addDependency(project, buildTypeName, dependency, closure)
        }
    }


    def addDependency(Project project, String buildTypeName, Dependency dependency, Closure closure) {
        if (Utils.isEmpty(buildTypeName)) {
            projectBuildTypes.each {
                String dependencySysName = DependenciesUtil.dependencySysName(project, it)
                addDependencyByBuildType(project, dependencySysName, dependency, closure)
            }
        } else {
            String dependencySysName = DependenciesUtil.dependencySysName(project, buildTypeName)
            addDependencyByBuildType(project, dependencySysName, dependency, closure)
        }
    }


    def addDependencyByBuildType(Project project, String dependencySysName, Dependency dependency, Closure closure) {
        ArrayList<DependencyModel> buildTypeDependencies = configDependencies[dependencySysName]
        if (buildTypeDependencies == null) {
            buildTypeDependencies = new ArrayList<>()
            configDependencies.put(dependencySysName, buildTypeDependencies)
        }
        DependencyModel dependencyModel = new DependencyModel()
        dependencyModel.dependency = dependency
        dependencyModel.configurationName = dependencySysName
        dependencyModel.configureClosure = closure
        buildTypeDependencies.add(dependencyModel)
    }


    /**
     *  为 Library 添加自定义依赖方法
     * @param project
     * @param plugin
     * @return
     */
    def attachMethod(Project project, LibraryPlugin plugin) {
        Class dependenciesClazz = project.dependencies.class
        ExpandoMetaClass expandoDependencies = new ExpandoMetaClass(dependenciesClazz, false)
        attachDependency(project,plugin,expandoDependencies)
        // add implementationComponent
        attachDependencyMethod(project, "", expandoDependencies)
        expandoDependencies.initialize()
        project.dependencies.metaClass = expandoDependencies
    }
    /**
     *  为 Application 添加自定义依赖方法
     * @param project
     * @param plugin
     * @return
     */
    def attachMethod(Project project, AppPlugin plugin) {
        Class dependenciesClazz = project.dependencies.class
        ExpandoMetaClass expandoDependencies = new ExpandoMetaClass(dependenciesClazz, false)
        // add ${buildType}ImplementationComponent
        attachDependency(project,plugin,expandoDependencies)
        attachDependencyMethod(project, "", expandoDependencies)
        // add implementationComponent
        expandoDependencies.initialize()
        project.dependencies.metaClass = expandoDependencies
    }

    /**
     * 适配 3.6.x  以上版本的问题
     * @param project
     * @param plugin
     * @param expandoDependencies
     * @return
     */
    def attachDependency(Project project,BasePlugin plugin,ExpandoMetaClass expandoDependencies){
        if(isNeedAdapterVersion(project)){
            plugin.extension.buildTypes.each {
                String buildTypeName =  it.name
                projectBuildTypes.add(buildTypeName)
                attachDependencyMethod(project, buildTypeName, expandoDependencies)
            }
        }else {
            plugin.variantManager.buildTypes.each {
                String buildTypeName = it.key
                projectBuildTypes.add(buildTypeName)
                attachDependencyMethod(project, buildTypeName, expandoDependencies)
            }
        }
    }
    /**
     * 适配 3.6.x  以上版本的问题
     * @param project
     * @return
     */
    static boolean  isNeedAdapterVersion(def project){
        String[] arrayVersion = project.gradle.gradleVersion.split("\\.")
        String version = new StringBuilder(arrayVersion[0] + "." + arrayVersion[1]).toString()
        float simpleVersion = version as float
        return simpleVersion >= 3.6F
    }
}
