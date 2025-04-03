import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

@OptIn(ExperimentalComposeLibrary::class)
dependencies {
    api(compose.components.resources)
    implementation(compose.runtime)
}

compose {
    resources {
        packageOfResClass = "dev.zt64.tau.resources"
        publicResClass = true
    }
}