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
    id("com.android.library") version "8.1.1" apply false
    id("org.jetbrains.kotlin.jvm") version "1.8.0" apply false
    id("com.google.firebase.crashlytics") version "2.9.9" apply false
}