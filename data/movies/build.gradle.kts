plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "tech.benhack.movies"
    compileSdk = ConfigurationData.compileSdk

    defaultConfig {
        minSdk = ConfigurationData.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )
            }
        }
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
    //Project
    implementation(project(":core:common"))
    implementation(project(":core:di"))
    implementation(project(":domain:movies"))

    //AndroidX
    implementation(Libs.AndroidX.core)
    implementation(Libs.AndroidX.appcompat)

    //Hilt
    implementation(Libs.Hilt.hilt)
    kapt(Libs.Hilt.compiler)

    //Room
    implementation(Libs.Room.room)
    implementation(Libs.Room.runtime)
    //It is not time to migrate: https://medium.com/@callmeryan/migrate-from-kapt-to-kotlin-ksp-not-the-best-time-yet-b30f8869da17
    //noinspection KaptUsageInsteadOfKsp
    kapt(Libs.Room.compiler)

    //Retrofit
    implementation(Libs.Retrofit.retrofit)
    implementation(Libs.Retrofit.gson)

    //Firebase Firestore
    implementation(Libs.Firebase.firestore)
    implementation(Libs.Firebase.functions)

    //Testing
    testImplementation(Libs.Testing.junit)
    testImplementation(Libs.Testing.coroutines)
    testImplementation(Libs.Testing.androidx)
    testImplementation(Libs.Testing.mockK)
    androidTestImplementation(Libs.Testing.junit_androidx)
    androidTestImplementation(Libs.Testing.espresso)
}

kapt {
    correctErrorTypes = true
}