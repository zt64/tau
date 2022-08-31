import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "1.7.10"
    id("org.jetbrains.compose") version "1.2.0-alpha01-dev774"
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    @OptIn(ExperimentalComposeLibrary::class)
    implementation(compose.material3)
    implementation(compose.desktop.linux_x64)

    implementation(compose.materialIconsExtended)

    implementation("com.github.ajalt.clikt:clikt:3.5.0")

    implementation("com.arkivanov.decompose:decompose:1.0.0-alpha-04")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:1.0.0-alpha-04")

    implementation("io.insert-koin:koin-core:3.2.0")
}


compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Deb, TargetFormat.AppImage)
            packageName = "tau"
            packageVersion = "1.0.0"
        }
    }
}