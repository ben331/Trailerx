plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.globant.auth"
    compileSdk = ConfigurationData.compileSdk

    defaultConfig {
        minSdk = ConfigurationData.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    implementation(Libs.AndroidX.core)
    implementation(Libs.Firebase.auth)
    implementation(Libs.Facebook.sdk)
    implementation(Libs.Google.auth)
    implementation(Libs.Firebase.auth)
    implementation(Libs.Firebase.ui)
    implementation(Libs.Firebase.firestore)
    implementation(Libs.Hilt.hilt)
    kapt(Libs.Hilt.compiler)
}

kapt {
    correctErrorTypes = true
}