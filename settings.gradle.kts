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
include(":data:auth")
include(":data:movies")
include(":core:common")
include(":domain:auth")
include(":domain:movies")
include(":feature:auth")
include(":feature:home")
