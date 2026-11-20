plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.spotless)
}

kotlin {
    js {
        moduleName = "minitus-worker"
        nodejs()
        useEsModules()
        binaries.library()
    }

    sourceSets {
        jsMain {
            dependencies {
                implementation(projects.shared)
                implementation(libs.minipavi.core.cloudflare)
            }
        }
    }
}

val sharedJvmRuntimeClasspath: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

dependencies {
    sharedJvmRuntimeClasspath(projects.shared) {
        attributes {
            attribute(
                org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType.attribute,
                org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType.jvm,
            )
            attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
        }
    }
}

/**
 * Prétraite le dictionnaire (normalisation, filtrage, tri) au moment de la compilation,
 * pour que le Worker n'ait plus qu'à découper le texte en lignes à son démarrage.
 */
val generateWordList by tasks.registering(JavaExec::class) {
    description = "Génère la liste de mots prétraitée embarquée dans le Worker."
    group = "build"

    val output = layout.buildDirectory.file("worker/words.txt")

    classpath = sharedJvmRuntimeClasspath
    mainClass.set("fr.outadoc.minitus.GenerateWordList")
    jvmArgs("-Dfile.encoding=UTF-8", "-Dstdout.encoding=UTF-8")
    argumentProviders.add(CommandLineArgumentProvider { listOf(output.get().asFile.absolutePath) })

    inputs.files(sharedJvmRuntimeClasspath)
    outputs.file(output)
}

/**
 * Compile tout ce dont `wrangler` a besoin : le code Kotlin/JS et la liste de mots.
 */
val workerDistribution by tasks.registering {
    description = "Compile le Worker Cloudflare et sa liste de mots."
    group = "build"

    dependsOn("jsNodeProductionLibraryDistribution", generateWordList)
}

spotless {
    kotlin {
        target("src/**/*.kt")
        ktlint()
    }
}
