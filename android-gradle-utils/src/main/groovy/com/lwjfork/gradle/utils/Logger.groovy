package com.lwjfork.gradle.utils

import groovy.transform.stc.ClosureParams
import org.gradle.api.Project
import groovy.transform.stc.FromString

class Logger {


    static Project project
    private static Closure<String> msgWrapper = { it ->
        return it
    }


    static def msgWrapper(@ClosureParams(value = FromString.class, options = ["java.lang.String"]) Closure closure) {
        msgWrapper = closure
    }

    static String wrapperMsg(String originMsg) {
        return msgWrapper.call(originMsg)
    }

    static d(String msg) {
        project.logger.debug(msg)
    }

    static i(String msg) {
        project.logger.info(msg)
    }

    static w(String msg) {
        project.logger.warn(msg)
    }

    static e(String msg) {
        project.logger.error(msg)
    }

    static l(String msg) {
        project.logger.lifecycle(msg)
    }


    static dw(String msg) {
        project.logger.debug(wrapperMsg(msg))
    }

    static iw(String msg) {
        project.logger.info(wrapperMsg(msg))
    }

    static ww(String msg) {
        project.logger.warn(wrapperMsg(msg))
    }

    static ew(String msg) {
        project.logger.error(wrapperMsg(msg))
    }

    static lw(String msg) {
        project.logger.lifecycle(wrapperMsg(msg))
    }

}
