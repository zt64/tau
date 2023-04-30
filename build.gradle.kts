import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvmToolchain(17)

    jvm()

    sourceSets {
        val commonMain by getting {
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            dependencies {
                implementation(compose.material3)
                implementation(compose.desktop.currentOs)
                implementation(compose.materialIconsExtended)
                implementation(compose.desktop.components.splitPane)

                implementation(libs.clikt)
                implementation(libs.decompose)
                implementation(libs.decompose.extensions.compose.jetbrains)
            }
        }
    }
}

compose.desktop.application {
    mainClass = "zt.tau.MainKt"

    nativeDistributions {
        packageName = "tau"
        description = "Compose file manager"
        packageVersion = "1.0.0"

        targetFormats(TargetFormat.Deb, TargetFormat.Exe, TargetFormat.Msi)

        linux {
            iconFile.set(file("resources/window-icon.png"))
        }

        windows {
            iconFile.set(file("resources/window-icon.ico"))
        }
    }
}