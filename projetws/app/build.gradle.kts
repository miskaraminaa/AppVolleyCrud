plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "miskar.ma.projetws"
    compileSdk = 34

    defaultConfig {
        applicationId = "miskar.ma.projetws"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // Glide library for image loading
    implementation("com.github.bumptech.glide:glide:5.0.0-rc01") {
        // To avoid using annotation processor
        exclude(group = "com.github.bumptech.glide", module = "compiler")
    }

    testImplementation ("junit:junit:4.12")
    implementation ("com.google.code.gson:gson:2.8.2")
    implementation ("com.github.bumptech.glide:glide:4.14.2") // Remplacez par la dernière version
    annotationProcessor("com.github.bumptech.glide:compiler:4.14.2") // Pour les annotations

    // Volley library for networking
    implementation("com.android.volley:volley:1.2.1")

    // Gson library for JSON parsing
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // Glide library for image loading
    implementation("com.github.bumptech.glide:glide:5.0.0-rc01") {
        // To avoid using annotation processor
        exclude(group = "com.github.bumptech.glide", module = "compiler")
    }

    testImplementation ("junit:junit:4.12")
    implementation ("com.google.code.gson:gson:2.8.2")
    implementation ("com.github.bumptech.glide:glide:4.14.2") // Remplacez par la dernière version
    annotationProcessor("com.github.bumptech.glide:compiler:4.14.2") // Pour les annotations

    // Volley library for networking
    implementation("com.android.volley:volley:1.2.1")

    // Gson library for JSON parsing
    implementation("com.google.code.gson:gson:2.11.0")
}