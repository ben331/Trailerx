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

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )
            }
        }
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

    //Google
    implementation(Libs.Google.material)
    implementation(Libs.Google.auth)

    //Tests
    testImplementation(Libs.Testing.junit)
    androidTestImplementation(Libs.Testing.junit_androidx)
    androidTestImplementation(Libs.Testing.espresso)
    androidTestImplementation(Libs.Testing.navigation)

    //Firebase
    implementation(Libs.Firebase.ui)
    implementation(Libs.Firebase.auth)
    implementation(Libs.Firebase.firestore)
    implementation(Libs.Firebase.storage)

    //Facebook
    implementation(Libs.Facebook.sdk)

    //Retrofit
    implementation(Libs.Retrofit.retrofit)
    implementation(Libs.Retrofit.gson)

    //Coroutines
    runtimeOnly(Libs.Coroutines.androidx)
    runtimeOnly(Libs.Coroutines.lifecycle)

    //Glide
    implementation(Libs.Glide.glide)

    //Dagger Hilt
    implementation(Libs.Hilt.hilt)
    kapt(Libs.Hilt.compiler)

    //Room
    implementation(Libs.Room.room)
    implementation(Libs.Room.runtime)
    //It is not time to migrate: https://medium.com/@callmeryan/migrate-from-kapt-to-kotlin-ksp-not-the-best-time-yet-b30f8869da17
    //noinspection KaptUsageInsteadOfKsp
    kapt(Libs.Room.compiler)
}

kapt {
    correctErrorTypes = true
}