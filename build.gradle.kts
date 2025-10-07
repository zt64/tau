import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ktlint) apply false
}

allprojects {
    apply {
        plugin("org.jlleitschuh.gradle.ktlint")
    }

    configure<KtlintExtension> {
        version = rootProject.libs.versions.ktlint
    }

    group = "dev.zt64.tau"
    version = "1.0.0"
}

subprojects {
    afterEvaluate {
        kotlinExtension.jvmToolchain(21)
    }

    dependencies {
        val ktlintRuleset by configurations

        ktlintRuleset(rootProject.libs.ktlint.rules.compose)
    }
}