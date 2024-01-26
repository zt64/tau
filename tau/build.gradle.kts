import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.ktlint)
}

kotlin {
    jvmToolchain(17)
}

@OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
dependencies {
    ktlintRuleset(libs.ktlint.rules.compose)

    implementation(compose.material3)
    implementation(compose.desktop.currentOs) {
        exclude(group = "org.jetbrains.compose.material", module = "material")
    }
    implementation(compose.desktop.components.splitPane)
    implementation(compose.materialIconsExtended)

    implementation(libs.coroutines.core)
    implementation(libs.material.kolor)
    implementation(libs.color.picker)
    implementation(libs.window.size)

    implementation(libs.clikt)
    implementation(libs.tika.core)
    implementation(libs.kfswatch)

    implementation(libs.bundles.dbus.java)
    implementation(libs.bundles.settings)

    implementation(libs.koin.core)
    implementation(libs.koin.compose)

    implementation(libs.voyager.navigator)
    implementation(libs.voyager.koin)
    implementation(libs.voyager.transitions)
}

compose.desktop.application {
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