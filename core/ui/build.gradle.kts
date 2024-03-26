plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "tech.benhack.ui"
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = ConfigurationData.composeCompiler
    }
}

dependencies {

    //AndroidX
    implementation(Libs.AndroidX.core)
    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.AndroidX.splash)
    implementation(Libs.Google.material)

    //Compose
    val composeBom = platform(Libs.Compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(Libs.Compose.material3)
    implementation(Libs.Compose.preview)
    debugImplementation(Libs.Compose.debugPreview)
    androidTestImplementation(Libs.Compose.junit4)
    debugImplementation(Libs.Compose.debugTesting)

    //Tests
    testImplementation(Libs.Testing.junit)
    androidTestImplementation(Libs.Testing.junit_androidx)
    androidTestImplementation(Libs.Testing.espresso)
    androidTestImplementation(Libs.Testing.navigation)

    //JWT
    implementation(Libs.JWT.api)
    implementation(Libs.JWT.impl)
    implementation(Libs.JWT.jackson)

    //Glide
    implementation(Libs.Glide.glide)

    //Dagger Hilt
    implementation(Libs.Hilt.hilt)
    kapt(Libs.Hilt.compiler)
}