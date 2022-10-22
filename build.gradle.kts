import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform") version "1.7.20"
    id("org.jetbrains.compose") version "1.3.0-alpha01-dev827"
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            @Suppress("OPT_IN_IS_NOT_ENABLED")
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            dependencies {
                implementation(compose.material3)
                implementation(compose.desktop.currentOs)
                implementation(compose.materialIconsExtended)
                implementation(compose.desktop.components.splitPane)

                implementation("com.github.ajalt.clikt:clikt:3.5.0")

                // Decompose
                val decomposeVersion = "1.0.0-alpha-06"
                implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:$decomposeVersion")
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "zt.tau.MainKt"

        nativeDistributions {
            packageName = "tau"
            description = "Compose file manager"
            packageVersion = "1.0.0"

            targetFormats(TargetFormat.Deb, TargetFormat.Exe, TargetFormat.Msi)

            linux {
                iconFile.set(project.file("resources/window-icon.png"))
            }

            windows {
                iconFile.set(project.file("resources/window-icon.ico"))
            }
        }
    }
}