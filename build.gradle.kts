import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "1.7.10"
    id("org.jetbrains.compose") version "1.2.0-beta02"
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    @Suppress("OPT_IN_IS_NOT_ENABLED")
    @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
    implementation(compose.material3)
    implementation(compose.desktop.linux_x64)
    implementation(compose.materialIconsExtended)

    implementation("com.github.ajalt.clikt:clikt:3.5.0")
}

compose.desktop {
    application {
        mainClass = "zt.tau.MainKt"

        nativeDistributions {
            packageName = "tau"
            packageVersion = "1.0.0"

            targetFormats(TargetFormat.AppImage, TargetFormat.Deb)
        }
    }
}
