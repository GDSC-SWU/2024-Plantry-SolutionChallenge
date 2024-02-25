import java.util.Properties

plugins {
    id("com.android.application")
    id ("com.google.gms.google-services")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization") version "1.7.20"
}

android {
    namespace = "com.plantry"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.plantry"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", Properties().apply {
                load(project.rootProject.file("local.properties").inputStream())
            }["base.url"].toString())
            buildConfigField("String", "AI_BASE_URL", Properties().apply {
                load(project.rootProject.file("local.properties").inputStream())
            }["ai_base.url"].toString())
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", Properties().apply {
                load(project.rootProject.file("local.properties").inputStream())
            }["base.url"].toString())
            buildConfigField("String", "AI_BASE_URL", Properties().apply {
                load(project.rootProject.file("local.properties").inputStream())
            }["ai_base.url"].toString())
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {

    // Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    // AndroidX
    implementation("androidx.activity:activity-ktx:1.1.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.navigation:navigation-fragment-ktx:1.5.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")

    // Matrial Design
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.navigation:navigation-fragment:2.7.6")
    implementation ("com.google.android.gms:play-services-auth:20.1.0")

    // Test Dependency
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    testImplementation("junit:junit:4.13.2")

    // Third-Party
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")
    implementation("com.squareup.okhttp3:okhttp-bom:4.9.0")
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation("io.coil-kt:coil:2.3.0")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("me.relex:circleindicator:2.1.6")
    implementation ("com.airbnb.android:lottie:3.7.0")

    // firebase
    implementation ("com.google.firebase:firebase-bom:32.7.2")
    implementation ("com.google.firebase:firebase-messaging:23.4.1")
    implementation ("com.google.firebase:firebase-analytics-ktx:21.1.1")
}