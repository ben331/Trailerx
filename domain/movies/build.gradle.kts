plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":core:common"))

    implementation(Libs.Coroutines.core)
    implementation(Libs.Hilt.dagger)

    //Testing
    testImplementation(Libs.Testing.junit)
    testImplementation(Libs.Testing.coroutines)
    testImplementation(Libs.Testing.mockK)
}