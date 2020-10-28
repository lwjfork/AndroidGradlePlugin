package com.lwjfork.gradle.transform


import com.android.build.api.transform.Context
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.api.ApplicationVariant
import com.lwjfork.gradle.model.VariantInfoModel
import org.gradle.api.Project

/**
 * Created by lwj on 2020-01-04.
 *  lwjfork@gmail.com
 */
abstract class BaseApplicationTransform extends BaseTransform {

    AppExtension appExtension
    AppPlugin appPlugin

    BaseApplicationTransform(Project project,AppPlugin appPlugin, AppExtension appExtension) {
        super(project)
        this.appExtension = appExtension
        this.appPlugin = appPlugin
    }


    final void delegateTransform(Context context,
                                 Collection<TransformInput> inputs,
                                 Collection<TransformInput> referencedInputs,
                                 TransformOutputProvider outputProvider,
                                 boolean isIncremental)
            throws IOException, TransformException, InterruptedException {
        AppPlugin appPlugin = project.plugins.getPlugin(AppPlugin)
        //变体
        VariantInfoModel variant = getVariationInfo(context,appExtension)
        delegateTransform(context, inputs, referencedInputs, outputProvider, isIncremental, variant)
    }


    abstract void delegateTransform(Context context,
                                    Collection<TransformInput> inputs,
                                    Collection<TransformInput> referencedInputs,
                                    TransformOutputProvider outputProvider,
                                    boolean isIncremental, VariantInfoModel variantInfoModel)
            throws IOException, TransformException, InterruptedException


    /**
     * 获取变体信息
     * @param context
     * @return 返回变体信息
     */
    final def getVariationInfo(Context context, AppExtension appExtension) {
        VariantInfoModel variantInfoModel = new VariantInfoModel()
        // 当前变体的名称
        String variantName = context.getVariantName()
        variantInfoModel.variantName = variantName
        ApplicationVariant applicationVariant = appExtension.applicationVariants.find {
            it.name == variantName
        }
        variantInfoModel.buildType = applicationVariant.getBuildType().name
        variantInfoModel.flavorName = applicationVariant.getFlavorName()
        variantInfoModel.applicationId = applicationVariant.applicationId
        variantInfoModel.versionName = applicationVariant.versionName
        variantInfoModel.versionCode = applicationVariant.versionCode
        variantInfoModel
    }

}
