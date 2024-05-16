plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt)
}

android {
    namespace = "dev.ohjiho.budgetify.setup"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Activity
    implementation(libs.androidx.activity.ktx)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)

    // Hilt
    implementation(libs.hilt.android.core)
    kapt(libs.hilt.compiler)
    testImplementation(libs.hilt.android.testing)
    kaptTest(libs.hilt.compiler)

    // Modules
    implementation(project(":core:theme"))
    implementation(project(":core:utils"))
    implementation(project(":domain"))
}