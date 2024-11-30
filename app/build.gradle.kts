plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.hydrateyou"
    compileSdk = 34 // Sesuaikan dengan SDK terbaru yang tersedia

    defaultConfig {
        applicationId = "com.example.hydrateyou"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // AndroidX libraries
    implementation("androidx.activity:activity-ktx:1.9.3")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.compose.material3:material3:1.1.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Compose and UI
    implementation(platform("androidx.compose:compose-bom:2024.10.00"))
    implementation("androidx.compose.ui:ui:1.5.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.3")
    implementation(libs.firebase.auth)
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.3")

    // Material Design
    implementation("com.google.android.material:material:1.9.0")

    // External libraries
    implementation("com.github.PhilJay:MPAndroidChart:3.0.3")
    // implementation("com.mikhaellopez:circularprogressbar:3.0.3") // Hapus jika tidak digunakan

    // Firebase
    implementation("com.google.firebase:firebase-database:20.2.2")
    implementation("com.google.firebase:firebase-analytics:21.4.0")
    implementation("com.google.firebase:firebase-auth:23.1.0")

    // Testing libraries
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.3")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.3")
}

// Aturan resolusi dependensi (dengan sintaks Kotlin yang benar)
configurations.all {
    resolutionStrategy {
        force("androidx.appcompat:appcompat:1.6.1")
        force("androidx.core:core-ktx:1.12.0")
        force("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    }
}
