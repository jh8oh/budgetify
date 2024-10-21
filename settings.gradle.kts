pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    val properties = File( "local.properties").inputStream().use {
        java.util.Properties().apply { load(it) }
    }

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven {
            url = uri("https://maven.pkg.github.com/jh8oh/currencypicker")
            credentials {
                username = properties.getProperty("gpr.user")
                password = properties.getProperty("gpr.key")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "Budgetify"
include(":app")
include(":core:utils")
include(":core:theme")
include(":data")
include(":domain")
include(":feature:overview")
include(":feature:finances")
include(":feature:settings")
include(":feature:stats")
include(":feature:setup")
include(":feature:account")
include(":feature:category")
