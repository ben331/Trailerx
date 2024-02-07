plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "tech.benhack.auth"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(project(":data:auth"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:di"))

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

    //Tests
    testImplementation(Libs.Testing.junit)
    androidTestImplementation(Libs.Testing.junit_androidx)
    androidTestImplementation(Libs.Testing.espresso)
    androidTestImplementation(Libs.Testing.navigation)

    //Facebook
    implementation(Libs.Facebook.sdk)

    //Coroutines
    runtimeOnly(Libs.Coroutines.androidx)
    runtimeOnly(Libs.Coroutines.lifecycle)

    //Dagger Hilt
    implementation(Libs.Hilt.hilt)
    kapt(Libs.Hilt.compiler)
}
kapt {
    correctErrorTypes = true
}