plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.globant.imdb"
    compileSdk = ConfigurationData.compileSdk

    defaultConfig {
        applicationId = ConfigurationData.applicationId
        minSdk = ConfigurationData.minSdk
        //noinspection OldTargetApi
        targetSdk = ConfigurationData.targetSdk
        versionCode = ConfigurationData.versionCode
        versionName = ConfigurationData.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = ConfigurationData.jvmTarget
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    // Project
    implementation(project(":feature:auth"))
    implementation(project(":feature:home"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))

    //  Kotlin
    implementation(Libs.Kotlin.stdlib)

    //AndroidX
    implementation(Libs.AndroidX.core)
    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.AndroidX.constraint)
    implementation(Libs.AndroidX.splash)

    //Navigation
    implementation(Libs.Navigation.fragment)
    implementation(Libs.Navigation.ui)
    implementation(Libs.Navigation.feature)
    implementation(Libs.Navigation.compose)

    //Facebook
    implementation(Libs.Facebook.sdk)

    //Dagger Hilt
    implementation(Libs.Hilt.hilt)
    kapt(Libs.Hilt.compiler)
}

kapt {
    correctErrorTypes = true
}