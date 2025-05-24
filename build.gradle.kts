// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    //id("com.google.dagger.hilt.android") version "2.51.1" apply false
    //id("com.google.devtools.ksp") version "1.9.23-1.0.20" apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    alias(libs.plugins.hilt).apply(false) // Áp dụng Hilt plugin từ catalog
    alias(libs.plugins.ksp).apply(false)
}