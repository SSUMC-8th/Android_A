// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.dagger.hilt.android") version "2.49" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false

    //이게추가된듯 firebase
    //alias(libs.plugins.google.gms.google.services) apply false
}

buildscript {
    repositories {
        google()
    }
    dependencies {

        //firebase
        classpath("com.google.gms:google-services:4.4.1")


        classpath ("com.android.tools.build:gradle:8.8.2")
        val nav_version = "2.7.7"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}