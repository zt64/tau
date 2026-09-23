plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ktlint)
}

dependencies {
    ktlintRuleset(libs.ktlint.rules.compose)

    implementation(projects.resources)

    implementation(libs.compose.icons)
    implementation(libs.kotlinx.io)
    implementation(libs.tika.core)
    implementation(libs.appdirs)
    implementation(libs.datastore)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.settings)

    testImplementation(libs.kotlin.test)
}