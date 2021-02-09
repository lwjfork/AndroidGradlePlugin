package com.lwjfork.gradle.adapter.util

import com.lwjfork.gradle.adapter.model.manifest.ActivityInfo
import com.lwjfork.gradle.adapter.model.manifest.ApplicationInfo
import groovy.util.slurpersupport.GPathResult
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil

class ReadManifest {
    static void readManifest(File file, ApplicationInfo applicationInfo) {
        XmlSlurper xmlSlurper = new XmlSlurper(false, false)
        GPathResult manifest = xmlSlurper.parse(file)
        //  application
        manifest.application.each { it ->
            applicationInfo.name = it.attributes().get('android:name')
        }
        // Activity
        manifest.application.activity.each { activity ->
            ActivityInfo activityInfo = new ActivityInfo()
            activityInfo.name = activity.attributes().get('android:name')
            applicationInfo.activities.add(activityInfo)
            applicationInfo.activityNames.add(activity.attributes().get('android:name'))
        }
        StreamingMarkupBuilder outputBuilder = new StreamingMarkupBuilder()
        String root = outputBuilder.bind {
            mkp.xmlDeclaration()
            mkp.yield manifest
        }
        String result = XmlUtil.serialize(root)
        file.text = result
    }
}
