object Versions {
    const val kotlin = "1.9.0"
    const val androidx_core = "1.12.0"
    const val androidx_appcompat = "1.6.1"
    const val androidx_constraint = "2.1.4"
    const val androidx_navigation = "2.7.3"
    const val androidx_splash = "1.0.1"
    const val androidx_junit = "4.13.2"
    const val androidx_test_junit = "1.1.5"
    const val androidx_espresso = "3.5.1"
    const val material = "1.9.0"
    const val room = "2.6.0"
    const val firebase_ui = "8.0.2"
    const val firebase_auth = "22.3.0"
    const val firebase_storage = "20.3.0"
    const val firebase_firestore = "24.10.0"
    const val firebase_analytics = "21.5.0"
    const val firebase_crashlytics = "18.6.1"
    const val firebase_perf = "20.5.1"
    const val firebase_remote_config = "21.6.0"
    const val firebase_functions = "20.4.0"
    const val jwt = "0.11.2"
    const val google_auth = "20.7.0"
    const val facebook_auth = "[8,9)"
    const val retrofit = "2.9.0"
    const val coroutine_core = "1.7.3"
    const val coroutine_androidx = "1.7.3"
    const val coroutine_lifecycle = "2.6.1"
    const val glide = "4.16.0"
    const val dagger = "2.50"
    const val hilt = "2.48.1"
    const val hilt_plugin = "2.44"
    const val google_services = "4.4.0"
    const val android_plugin = "1.8.0"
    const val application_plugin = "8.1.1"
    const val swipe_refresh = "1.1.0"
    const val multidex = "2.0.1"
    const val coroutine_test = "1.6.4"
    const val mockK = "1.12.0"
    const val androidx_test = "2.2.0"
    const val compose = "2024.03.00"
    const val composeRuntime = "1.6.4"
    const val constraintLayout = "1.0.1"
}

object Libs {
    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    }

    object AndroidX {
        const val core = "androidx.core:core-ktx:${Versions.androidx_core}"
        const val appcompat = "androidx.appcompat:appcompat:${Versions.androidx_appcompat}"
        const val constraint = "androidx.constraintlayout:constraintlayout:${Versions.androidx_constraint}"
        const val splash = "androidx.core:core-splashscreen:${Versions.androidx_splash}"
        const val navigation_plugin = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.androidx_navigation}"
        const val multidex = "androidx.multidex:multidex:${Versions.multidex}"
    }

    object Navigation {
        const val fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.androidx_navigation}"
        const val ui = "androidx.navigation:navigation-ui-ktx:${Versions.androidx_navigation}"
        const val feature = "androidx.navigation:navigation-dynamic-features-fragment:${Versions.androidx_navigation}"
        const val compose = "androidx.navigation:navigation-compose:${Versions.androidx_navigation}"
    }

    object Layouts {
        const val swipe_refresh = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipe_refresh}"
    }

    object Testing {
        const val junit = "junit:junit:${Versions.androidx_junit}"
        const val junit_androidx = "androidx.test.ext:junit:${Versions.androidx_test_junit}"
        const val espresso = "androidx.test.espresso:espresso-core:${Versions.androidx_espresso}"
        const val navigation = "androidx.navigation:navigation-testing:${Versions.androidx_navigation}"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutine_test}"
        const val androidx = "androidx.arch.core:core-testing:${Versions.androidx_test}"
        const val mockK = "io.mockk:mockk:${Versions.mockK}"
    }

    object Firebase {
        const val ui = "com.firebaseui:firebase-ui-auth:${Versions.firebase_ui}"
        const val auth = "com.google.firebase:firebase-auth:${Versions.firebase_auth}"
        const val storage = "com.google.firebase:firebase-storage:${Versions.firebase_storage}"
        const val firestore = "com.google.firebase:firebase-firestore:${Versions.firebase_firestore}"
        const val analytics = "com.google.firebase:firebase-analytics:${Versions.firebase_analytics}"
        const val crashlytics = "com.google.firebase:firebase-crashlytics:${Versions.firebase_crashlytics}"
        const val performance = "com.google.firebase:firebase-perf:${Versions.firebase_perf}"
        const val remote_config = "com.google.firebase:firebase-config:${Versions.firebase_remote_config}"
        const val functions = "com.google.firebase:firebase-functions-ktx:${Versions.firebase_functions}"
    }

    object JWT {
        const val api = "io.jsonwebtoken:jjwt-api:${Versions.jwt}"
        const val impl = "io.jsonwebtoken:jjwt-impl:${Versions.jwt}"
        const val jackson = "io.jsonwebtoken:jjwt-jackson:${Versions.jwt}"
    }

    object Google {
        const val material = "com.google.android.material:material:${Versions.material}"
        const val auth = "com.google.android.gms:play-services-auth:${Versions.google_auth}"
    }

    object Compose {
        const val bom = "androidx.compose:compose-bom:${Versions.compose}"
        const val runtime = "androidx.compose.runtime:runtime:${Versions.composeRuntime}"

        // Choose one of the following for ui
        const val material3 = "androidx.compose.material3:material3"
        const val material2 = "androidx.compose.material:material"
        const val foundationalComponents = "androidx.compose.foundation:foundation"
        const val ui = "androidx.compose.ui:ui"

        //Android Preview
        const val preview = "androidx.compose.ui:ui-tooling-preview"
        const val debugPreview = "androidx.compose.ui:ui-tooling"

        //Testing
        const val junit4 = "androidx.compose.ui:ui-test-junit4"
        const val debugTesting = "androidx.compose.ui:ui-test-manifest"

        object Optionals {
            const val icons = "androidx.compose.material:material-icons-core"
            const val iconsExtended = "androidx.compose.material:material-icons-extended"
            const val windowSizeUtils = "androidx.compose.material3:material3-window-size-class"
            const val activities = "androidx.activity:activity-compose:1.8.2"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1"
            const val liveData = "androidx.compose.runtime:runtime-livedata"
            const val rxjava = "androidx.compose.runtime:runtime-rxjava2"
            const val constraints = "androidx.constraintlayout:constraintlayout-compose:${Versions.constraintLayout}"
        }
    }

    object Facebook {
        const val sdk = "com.facebook.android:facebook-android-sdk:${Versions.facebook_auth}"
    }

    object Retrofit {
        const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
        const val gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    }

    object Coroutines {
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutine_core}"
        const val androidx = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutine_androidx}"
        const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.coroutine_lifecycle}"
    }

    object Glide {
        const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    }

    object Hilt {
        const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
        const val compiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
        const val plugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
        const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    }

    object Room {
        const val room = "androidx.room:room-ktx:${Versions.room}"
        const val runtime = "androidx.room:room-runtime:${Versions.room}"
        const val compiler = "androidx.room:room-compiler:${Versions.room}"
    }
}