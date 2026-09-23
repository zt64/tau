plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

dependencies {
    api(libs.compose.resources)
    implementation(libs.compose.runtime)
}

compose {
    resources {
        packageOfResClass = "dev.zt64.tau.resources"
        publicResClass = true
    }
}