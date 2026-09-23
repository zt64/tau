import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.stability.analyzer)
}

dependencies {
    ktlintRuleset(libs.ktlint.rules.compose)

    implementation(projects.core)
    implementation(projects.ui)
    implementation(projects.resources)

    implementation(compose.desktop.currentOs) {
        exclude(group = "org.jetbrains.compose.material", module = "material")
    }

    implementation(libs.clikt)
    implementation(libs.bundles.koin)

    testImplementation(libs.compose.ui.test)
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