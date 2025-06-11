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
        viewBinding = true;
    }
}

dependencies {
    implementation ("com.tbuonomo:dotsindicator:4.3")
    implementation("com.github.bumptech.glide:glide:4.14.2")
    //implementation(libs.legacy.support.v4)
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.legacy.support.v4)
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
    implementation("me.relex:circleindicator:2.1.6")
    implementation ("com.google.android.material:material:1.10.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
}