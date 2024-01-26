plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.resources) apply false
    alias(libs.plugins.ktlint)
}

ktlint {
    version = "1.1.0"
}