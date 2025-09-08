plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt") // ✅ Correct in Kotlin DSL
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    alias(libs.plugins.kotlin.compose)
}


android {
    namespace = "com.unicred"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.unicred"
        minSdk = 26 // Changed from 24 to 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        languageVersion = "2.0" // Updated from 1.9
        apiVersion = "2.0"     // Updated from 1.9
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        // kotlinCompilerExtensionVersion = "1.5.11" // Removed to use version from compose plugin
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/gradle/incremental.annotation.processors" // Added exclusion
        }
    }
    // buildToolsVersion = "36.0.0" // Removed
}


dependencies {
    implementation("com.google.android.material:material:1.12.0") // Added Material Components for XML themes
    implementation("androidx.core:core-ktx:1.12.0") // Consider using libs.androidx.core.ktx if defined
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0") // Consider using libs.androidx.lifecycle.runtime.ktx if defined
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0") // Added for collectAsStateWithLifecycle
    implementation("androidx.activity:activity-compose:1.8.2") // Consider using libs.androidx.activity.compose if defined
    implementation(platform(libs.androidx.compose.bom)) // Updated to use version catalog
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class")
    
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.5")
    
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    
    // Hilt for Dependency Injection
    implementation("com.google.dagger:hilt-android:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    kapt("com.google.dagger:hilt-android-compiler:2.48") // Changed from implementation to kapt

    // Retrofit for API calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    
    // Icons
    implementation("androidx.compose.material:material-icons-extended:1.5.4")
    
    // System UI Controller
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")
    
    // Testing
    testImplementation("junit:junit:4.13.2") // Consider using libs.junit if defined
    androidTestImplementation("androidx.test.ext:junit:1.1.5") // Consider using libs.androidx.junit if defined
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") // Consider using libs.androidx.espresso.core if defined
    androidTestImplementation(platform(libs.androidx.compose.bom)) // Updated to use version catalog
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    // implementation(libs.hilt.android) // Removed
    // kapt(libs.hilt.compiler) // Removed
}