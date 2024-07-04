import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.libres)
}

@OptIn(ExperimentalComposeLibrary::class)
dependencies {
    ktlintRuleset(libs.ktlint.rules.compose)

    implementation(compose.material3)
    implementation(compose.desktop.currentOs) {
        exclude(group = "org.jetbrains.compose.material", module = "material")
    }
    implementation(compose.desktop.components.splitPane)
    implementation(compose.materialIconsExtended)

    implementation(libs.coroutines.core)
    implementation(libs.materialKolor)
    implementation(libs.colorPicker)
    implementation(libs.windowSize)

    implementation(libs.clikt)
    implementation(libs.tika.core)
    implementation(libs.kfswatch)

    implementation(libs.io)
    implementation(libs.oshi)

    implementation(libs.bundles.voyager)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.dbus.java)
    implementation(libs.bundles.settings)

    implementation(libs.libres.compose)

    testImplementation(compose.uiTest)
    testImplementation(kotlin("test"))
}

compose {
    desktop.application {
        mainClass = "dev.zt64.tau.MainKt"

        nativeDistributions {
            packageName = "tau"
            description = "Compose file manager"
            packageVersion = "1.0.0"

            modules(
                "java.base",
                "java.instrument",
                "java.management",
                "java.prefs"
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
}

ktlint {
    filter {
        exclude("**/libres/**.kt")
    }
}