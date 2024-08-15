plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.cityquest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cityquest"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}




    dependencies {
        // Core AndroidX libraries
        implementation("androidx.appcompat:appcompat:1.6.1")
        implementation("androidx.activity:activity:1.9.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("androidx.core:core-ktx:1.10.1")

        // Material Design
        implementation("com.google.android.material:material:1.12.0")

        // Firebase
        implementation("com.google.firebase:firebase-auth:23.0.0")
        implementation("com.google.firebase:firebase-firestore:25.0.0")
        implementation(files("libs/OlaMapSdk.aar"))
        implementation(files("libs/OlaMapSdk.aar"))
        implementation(files("libs\\classes.jar"))

        // Testing
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

        // Additional Libraries
        implementation("io.github.architshah248.calendar:awesome-calendar:2.0.0")
        implementation("com.google.android.flexbox:flexbox:3.0.0")
        implementation("com.github.bumptech.glide:glide:4.16.0")

        // MapLibre SDK
        implementation("org.maplibre.gl:android-sdk:10.2.0")
        implementation("org.maplibre.gl:android-sdk-directions-models:5.9.0")
        implementation("org.maplibre.gl:android-sdk-services:5.9.0")
        implementation("org.maplibre.gl:android-sdk-turf:5.9.0")
        implementation("org.maplibre.gl:android-plugin-markerview-v9:1.0.0")
        implementation("org.maplibre.gl:android-plugin-annotation-v9:1.0.0")

        // Lifecycle libraries
        implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
        implementation ("androidx.lifecycle:lifecycle-compiler:2.0.0")
        implementation ("androidx.lifecycle:lifecycle-viewmodel:2.5.1") // Ensure this is up-to-date
        implementation ("androidx.lifecycle:lifecycle-livedata:2.5.1") // Ensure this is up-to-date
        implementation("androidx.startup:startup-runtime:1.1.1")


        // Ola Maps SDK

        implementation ("com.moengage:moe-android-sdk:12.6.01")

        implementation ("com.squareup.retrofit2:retrofit:2.9.0")
        implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation ("com.squareup.okhttp3:okhttp:4.10.0")







    }







