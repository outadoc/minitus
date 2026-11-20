plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.spotless)
}

kotlin {
    jvm()

    js {
        nodejs()
    }

    sourceSets {
        commonMain {
            dependencies {
                api(libs.minipavi.core)
                api(libs.minipavi.videotex)
                api(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization.json)
            }
        }

        jsMain {
            dependencies {
                implementation(npm("@js-joda/timezone", "2.23.0"))
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        jvmTest {
            dependencies {
                implementation(libs.kotlin.test.junit)
            }
        }
    }
}

spotless {
    kotlin {
        target("src/**/*.kt")
        ktlint()
    }
}
