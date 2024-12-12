import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude

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

    buildFeatures {
        buildConfig = true
        viewBinding = true
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
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.29")
    implementation("com.airbnb.android:lottie:6.0.0")



    // Material Design
    implementation("com.google.android.material:material:1.12.0")

    // Firebase

    implementation("com.google.firebase:firebase-firestore:25.0.0")
    implementation("androidx.gridlayout:gridlayout:1.0.0")

    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation("androidx.navigation:navigation-fragment:2.6.0")
    implementation("androidx.navigation:navigation-ui:2.6.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")


    // Local JAR/AAR files

    //implementation(files("libs/Places-sdk-2.3.6.jar"))

    implementation ("com.google.android.flexbox:flexbox:3.0.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Additional Libraries
    implementation("io.github.architshah248.calendar:awesome-calendar:2.0.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")

//    // Ola Maps SDK
//    implementation("com.moengage:moe-android-sdk:12.6.01")
    //OlaMap SDK
    implementation(files("libs/OlaMapSdk.aar"))

    //Maplibre
    implementation ("org.maplibre.gl:android-sdk:10.0.2")
    implementation ("org.maplibre.gl:android-plugin-annotation-v9:1.0.0")
    implementation ("org.maplibre.gl:android-plugin-markerview-v9:1.0.0")

    // Volley
    implementation("com.android.volley:volley:1.2.1")




    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
//    implementation("com.google.firebase:firebase-auth")

    implementation ("com.google.android.libraries.places:places:3.5.0")

    implementation ("com.google.maps:google-maps-services:2.2.0")
    implementation ("org.slf4j:slf4j-simple:1.7.25")

    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")




}
