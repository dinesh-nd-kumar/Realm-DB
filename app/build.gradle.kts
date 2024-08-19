plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id ("kotlin-kapt") // add this line
    id ("realm-android")
    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin) // add this line
//    alias ( libs.plugins.realm)
}

android {
    namespace = "com.dineshdk.realmdb"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dineshdk.realmdb"
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
    buildFeatures{
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
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.maps)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
//    implementation ("io.realm:realm-gradle-plugin:10.16.0")
//    implementation ("io.realm.kotlin:library-base:1.16.0")
    // If using Device Sync
//    implementation ("io.realm.kotlin:library-sync:1.16.0")
    // If using coroutines with the SDK
//    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0")


        val work_version = "2.9.1"

        // (Java only)
//        implementation("androidx.work:work-runtime:$work_version")

        // Kotlin + coroutines
        implementation("androidx.work:work-runtime-ktx:$work_version")

        // optional - RxJava2 support
//        implementation("androidx.work:work-rxjava2:$work_version")

        // optional - GCMNetworkManager support
//        implementation("androidx.work:work-gcm:$work_version")

        // optional - Test helpers
//        androidTestImplementation("androidx.work:work-testing:$work_version")

        // optional - Multiprocess support
//        implementation("androidx.work:work-multiprocess:$work_version")
    implementation ("com.google.android.gms:play-services-location:21.0.1") // For location services
}