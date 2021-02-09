package com.lwjfork.aop.utils;

import java.util.concurrent.Callable;

/**
 * Created by lwj on 2020/8/7.
 * lwjfork@gmail.com
 */
@SuppressWarnings("unused")
public class TaskUtil {

    //得到一个任务
    @SuppressWarnings("unused")
   public static Callable<Void> getTask(Runnable runnable) {
        return () -> {
            runnable.run();
            return null;
        };
    }
}
