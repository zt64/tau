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

dependencies {
    ktlintRuleset(libs.ktlint.rules.compose)

    implementation(projects.core)
    implementation(projects.resources)

    implementation(libs.compose.material3)
    implementation(libs.compose.material3.windowSizeClass)
    implementation(libs.compose.material3.adaptiveNavSuite)
    implementation(libs.compose.icons)
    implementation(libs.compose.splitPane)
    implementation(libs.compose.preview)

    implementation(libs.humanReadable)
    implementation(libs.viewmodel)
    implementation(libs.kfswatch)
    implementation(libs.oshi)

    implementation(libs.materialKolor)
    implementation(libs.composePipette)
    implementation(libs.reorderable)

    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.koin)

    testImplementation(libs.compose.ui.test)
    testImplementation(libs.kotlin.test)
}