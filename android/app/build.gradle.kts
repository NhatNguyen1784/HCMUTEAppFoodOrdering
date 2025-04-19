plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "vn.hcmute.appfoodorder"
    compileSdk = 35

    defaultConfig {
        applicationId = "vn.hcmute.appfoodorder"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures{
        dataBinding = true;
    }
}

dependencies {
    implementation("com.github.bumptech.glide:glide:4.14.2")
    implementation(libs.legacy.support.v4)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    annotationProcessor("com.github.bumptech.glide:compiler:4.14.2")
    implementation(libs.pinview)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.fragment)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("androidx.core:core-splashscreen:1.0.1")
}