// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Libs.AndroidX.navigation_plugin)
        classpath(Libs.Hilt.plugin)
    }
}

plugins {
    id("com.android.application") version Versions.application_plugin apply false
    id("org.jetbrains.kotlin.android") version Versions.android_plugin apply false
    id("com.google.gms.google-services") version Versions.google_services apply false
    id("com.google.dagger.hilt.android") version Versions.hilt_plugin apply false
}