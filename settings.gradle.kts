rootProject.name = "minitus"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()

        // Pour tester une version locale de MiniPavi-Kotlin (publishToMavenLocal),
        // lancez Gradle avec -Pminipavi.mavenLocal=true
        // (ou GRADLE_OPTS="-Dorg.gradle.project.minipavi.mavenLocal=true" depuis npm).
        if (providers.gradleProperty("minipavi.mavenLocal").orNull == "true") {
            mavenLocal {
                mavenContent {
                    includeGroup("fr.outadoc.minipavi")
                }
            }
        }

        // MiniPavi-Kotlin est publié sur GitHub Packages, qui demande une authentification même
        // pour les paquets publics. Fournissez un jeton avec la portée `read:packages`, soit via
        // les propriétés Gradle `gpr.user` et `gpr.key` (dans ~/.gradle/gradle.properties), soit
        // via les variables d'environnement GITHUB_ACTOR et GITHUB_TOKEN (comme sur GitHub Actions).
        maven {
            name = "MiniPaviGitHubPackages"
            url = uri("https://maven.pkg.github.com/outadoc/minipavi-kotlin")
            credentials {
                username = providers.gradleProperty("gpr.user").orElse(providers.environmentVariable("GITHUB_ACTOR")).orNull
                password = providers.gradleProperty("gpr.key").orElse(providers.environmentVariable("GITHUB_TOKEN")).orNull
            }
            mavenContent {
                includeGroup("fr.outadoc.minipavi")
            }
        }
    }
}

include(":shared")
include(":service")
include(":worker")
