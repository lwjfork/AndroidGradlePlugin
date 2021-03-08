package com.lwjfork.android.gradle.aop.transform

import com.android.build.api.transform.*
import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.internal.pipeline.TransformManager
import com.lwjfork.android.gradle.aop.task.CommonAopAspect
import com.lwjfork.aop.utils.FileUtil
import com.lwjfork.gradle.adapter.model.VariantInfoModel
import com.lwjfork.gradle.adapter.transform.BaseApplicationTransform
import org.gradle.api.Project

class CommonAopTransform extends BaseApplicationTransform{


    CommonAopTransform(Project project, AppPlugin appPlugin, AppExtension appExtension) {
        super(project, appPlugin, appExtension)
    }



    @Override
    void delegateTransform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental, VariantInfoModel variantInfoModel) throws IOException, TransformException, InterruptedException {
        def clearCache = !isIncremental
        if (clearCache) {
            outputProvider.deleteAll()
        }
        boolean  isNeedAspect = false
        // 所有开启了 代码分析植入的 task 集合，key-taskName，value-task
        HashMap<String,CommonAopAspect> tasks = new HashMap<>()
        project.tasks.withType(CommonAopAspect.class).forEach{
            it.variantInfoModel = variantInfoModel
            it.appExtension = appExtension
            it.context = context
            //  若task开启代码分析，则收集task方便下一步进行代码分析植入
            def taskAspectAble = it.isNeedAspect()
            if(taskAspectAble){
                tasks.put(it.name,it)
            }
            isNeedAspect = isNeedAspect || taskAspectAble
        }
        // 若无需进行代码植入分析，则直接复制即可
        if(!isNeedAspect){
          onlyCopy(inputs,outputProvider)
            return
        }
        // 代码分析并进行代码植入
        CommonAnalyzer analyzer = new CommonAnalyzer(project, this.appExtension, variantInfoModel, outputProvider)
        tasks.each {
            def task = it.value
            task.analyzer = analyzer
            task.initAnalyzer()
        }
        inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput directoryInput ->
                analyzer.addDirectoryInput(directoryInput)
            }
            input.jarInputs.each { JarInput jarInput ->
                analyzer.addJarInput(jarInput)
            }
        }
        analyzer.execute()
    }

    protected void onlyCopy(Collection<TransformInput> inputs, TransformOutputProvider outputProvider) {
        inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput directoryInput ->
                File destFile = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                FileUtil.copyDirectory(directoryInput.file.absolutePath, destFile.absolutePath)
            }
            input.jarInputs.each { JarInput jarInput ->
                File destFile = outputProvider.getContentLocation(jarInput.name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtil.copyFile(jarInput.file.absolutePath, destFile.absolutePath)
            }
        }
    }


    @Override
    String getName() {
        return "CommonAopTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    // TODO should to support Incremental  return true
    @Override
    boolean isIncremental() {
        return false
    }
}
