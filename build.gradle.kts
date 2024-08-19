buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {

        classpath("io.realm:realm-gradle-plugin:10.16.0")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin) apply false
//    alias ( libs.plugins.realm) apply false
}