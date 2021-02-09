package com.lwjfork.gradle.adapter.transform


import com.android.build.api.transform.Context
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.lwjfork.gradle.adapter.model.VariantInfoModel
import com.lwjfork.gradle.adapter.util.VariantUtil
import org.gradle.api.Project

/**
 * Created by lwj on 2020-01-04.
 *  lwjfork@gmail.com
 */
abstract class BaseApplicationTransform extends Transform {

    AppExtension appExtension
    AppPlugin appPlugin
    Project project

    BaseApplicationTransform(Project project,AppPlugin appPlugin, AppExtension appExtension) {
        this.project = project
        this.appExtension = appExtension
        this.appPlugin = appPlugin
    }


    @Override
    final void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        def clearCache = !isIncremental
        // 非增量更新删除上次构建信息
        if (clearCache) {
            outputProvider.deleteAll()
        }
        VariantInfoModel variant = VariantUtil.getVariationInfo(context,appExtension)
        delegateTransform(context, inputs, referencedInputs, outputProvider, isIncremental,variant)
    }

    abstract void delegateTransform(Context context,
                                    Collection<TransformInput> inputs,
                                    Collection<TransformInput> referencedInputs,
                                    TransformOutputProvider outputProvider,
                                    boolean isIncremental, VariantInfoModel variantInfoModel)
            throws IOException, TransformException, InterruptedException
}
