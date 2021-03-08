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
        project.tasks.withType(CommonAopAspect.class).forEach{
            it.variantInfoModel = variantInfoModel
            it.appExtension = appExtension
            isNeedAspect = it.isNeedAspect()
        }
        if(!isNeedAspect){
          onlyCopy(inputs,outputProvider)
            return
        }
        CommonAnalyzer analyzer = new CommonAnalyzer(project, this.appExtension, variantInfoModel, outputProvider)
        project.tasks.withType(CommonAopAspect.class).forEach{
            it.analyzer = analyzer
            it.initAnalyzer()
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
