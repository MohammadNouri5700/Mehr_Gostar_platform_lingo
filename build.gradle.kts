// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.google.protobuf:protobuf-gradle-plugin:0.9.4")
    }
}



plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.org.jetbrains.kotlin.kapt) apply false
    id ("org.jetbrains.kotlin.jvm") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.google.firebase.crashlytics") version "3.0.2" apply false
    id("com.google.firebase.firebase-perf") version "1.4.2" apply false
}
