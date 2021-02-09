package com.lwjfork.gradle.adapter.model.manifest

class ApplicationInfo {
    String name
    ArrayList<String> activityNames = new ArrayList<>()
    ArrayList<ActivityInfo> activities = new ArrayList<>()

    @Override
    public String toString() {
        return "ApplicationInfo{" +
                "name='" + name + '\'' +
                ", activities=" + activities.toString() +
                '}';
    }
}
