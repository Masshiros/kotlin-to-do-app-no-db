// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {

    id ("org.jetbrains.kotlin.android") version "1.9.10" apply false
}
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.6.0")
        // Note: Only build script classpath dependencies should be here
    }
}



tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}

