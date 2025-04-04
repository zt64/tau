import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ktlint)
}

kotlin {
    compilerOptions {
        optIn.addAll(
            "androidx.compose.ui.ExperimentalComposeUiApi",
            "androidx.compose.material3.ExperimentalMaterial3Api",
            "androidx.compose.foundation.ExperimentalFoundationApi",
            "org.koin.core.annotation.KoinExperimentalAPI"
        )
    }
}

@OptIn(ExperimentalComposeLibrary::class)
dependencies {
    ktlintRuleset(libs.ktlint.rules.compose)

    implementation(project(":core"))
    implementation(project(":resources"))

    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
    implementation(compose.material3AdaptiveNavigationSuite)
    implementation(compose.desktop.components.splitPane)
    implementation(compose.preview)

    implementation(libs.humanReadable)
    implementation(libs.viewmodel)
    implementation(libs.tika.core)
    implementation(libs.kfswatch)
    implementation(libs.oshi)

    implementation(libs.materialKolor)
    implementation(libs.composePipette)
    implementation(libs.windowSize)
    implementation(libs.reorderable)

    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.koin)

    testImplementation(compose.uiTest)
    testImplementation(libs.kotlin.test)
}

composeCompiler {
    featureFlags.add(ComposeFeatureFlag.OptimizeNonSkippingGroups)
}