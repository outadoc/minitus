import io.ktor.plugin.features.DockerImageRegistry
import io.ktor.plugin.features.DockerPortMapping

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.spotless)
    application
}

application {
    mainClass.set("fr.outadoc.minitus.ApplicationKt")
    applicationDefaultJvmArgs = listOf(
        "-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}"
    )
}

ktor {
    docker {
        localImageName = "minitus"
        jreVersion = JavaVersion.VERSION_21
        portMappings = listOf(
            DockerPortMapping(
                outsideDocker = 8080,
                insideDocker = 8080
            )
        )
        externalRegistry = DockerImageRegistry.externalRegistry(
            username = provider { System.getenv("GHCR_USERNAME") },
            password = provider { System.getenv("GHCR_PASSWORD") },
            hostname = provider { "ghcr.io" },
            project = provider { "minitus" },
            namespace = provider { "outadoc" },
        )
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.minipavi.core.ktor)

    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
}

spotless {
    kotlin {
        ktlint()
    }
}
