
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.hotReload)
    alias(libs.plugins.ktlint)
}

@OptIn(ExperimentalComposeLibrary::class)
dependencies {
    ktlintRuleset(libs.ktlint.rules.compose)

    implementation(project(":core"))
    implementation(project(":ui"))
    implementation(project(":resources"))

    implementation(compose.desktop.currentOs) {
        exclude(group = "org.jetbrains.compose.material", module = "material")
    }

    implementation(libs.clikt)

    implementation(libs.bundles.koin)

    testImplementation(compose.uiTest)
    testImplementation(libs.kotlin.test)
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
                iconFile = file("icon/window-icon.png")
            }

            windows {
                iconFile = file("icon/window-icon.ico")
            }
        }

        buildTypes.release.proguard {
            configurationFiles.from(project.file("proguard-rules.pro"))
            obfuscate = true
        }
    }
}