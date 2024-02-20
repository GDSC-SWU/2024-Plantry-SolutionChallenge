// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.1.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
        classpath ("com.google.gms:google-services:4.4.1")
    }

}


val clean by tasks.registering(Delete::class) {
    delete(rootProject.buildDir)
}