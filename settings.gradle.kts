pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "IMDb"
include(":app")
include(":feature:home")
include(":feature:auth")
include(":domain:movies")
include(":domain:auth")
include(":data:movies")
include(":data:auth")
include(":core:common")
include(":core:ui")