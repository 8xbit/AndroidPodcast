plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.podcatsapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.podcatsapp"
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
    // Dependencias principales
    implementation("com.github.bumptech.glide:glide:4.15.1") // Glide
    implementation("com.github.bumptech.glide:annotation:4.15.1") // Glide annotations (opcional, útil para GlideApp)

    // Si usas Glide con soporte para VideoView y otros casos avanzados:
    implementation("com.github.bumptech.glide:okhttp3-integration:4.15.1") // Integración de Glide con OkHttp (si usas OkHttp)

    // Si usas la versión de Glide para cargar imágenes y necesitas el manejo de imágenes como caché, etc.
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1") // Procesador de anotaciones de Glide

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}