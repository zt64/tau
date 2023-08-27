import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.resources)
    alias(libs.plugins.ktlint)
}

kotlin {
    jvmToolchain(17)

    jvm()

    sourceSets {
        commonMain {
            @OptIn(ExperimentalComposeLibrary::class)
            dependencies {
                implementation(compose.material3)
                implementation(compose.desktop.currentOs)
                implementation(compose.materialIconsExtended)
                implementation(compose.desktop.components.splitPane)

                implementation(libs.bundles.dbus.java)
                implementation(libs.bundles.settings)

                implementation(libs.clikt)
                implementation(libs.tika.core)
                implementation(libs.kfswatch)
                implementation(libs.compose.icons)

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

                api(libs.bundles.moko.resources)
            }
        }

        val jvmMain by getting {
            dependsOn(commonMain.get())
        }
    }
}

dependencies {
    ktlintRuleset(libs.ktlint.rules.compose)
}

multiplatformResources {
    multiplatformResourcesPackage = "zt.tau"
    multiplatformResourcesClassName = "R"
}

compose.desktop.application {
    mainClass = "zt.tau.MainKt"

    nativeDistributions {
        packageName = "tau"
        description = "Compose file manager"
        packageVersion = "1.0.0"

        modules(
            "java.base",
            "java.instrument",
            "java.management",
            "java.prefs",
            "java.sql",
            "jdk.unsupported",
            "jdk.xml.dom"
        )

        targetFormats(TargetFormat.Deb, TargetFormat.Exe, TargetFormat.Msi)

        linux {
            iconFile = rootProject.file("resources/window-icon.png")
        }

        windows {
            iconFile = rootProject.file("resources/window-icon.ico")
        }

        buildTypes.release.proguard {
            configurationFiles.from(project.file("proguard-rules.pro"))
            obfuscate = true
        }
    }
}