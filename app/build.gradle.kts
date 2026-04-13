plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("com.google.devtools.ksp")

    alias(libs.plugins.kotlin.serialization)

}

android {
    namespace = "com.example.propertymanagement"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.propertymanagement"
        minSdk = 26
        targetSdk = 36
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.ui)
    implementation(libs.androidx.compose.foundation)
    // Navigation Compose
    val nav_version = "2.8.0-beta01"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    //DI
    val koin_version = "3.5.3"
    implementation("io.insert-koin:koin-android:$koin_version")
    implementation("io.insert-koin:koin-androidx-compose:$koin_version")
    testImplementation("io.insert-koin:koin-test:$koin_version")

    // Retrofit
    val retrofit_version = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation("com.andretietz.retrofit:cache-extension:1.0.0")

    //YandexMaps
    implementation("com.yandex.android:maps.mobile:4.30.0-lite")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")


    implementation("com.google.android.gms:play-services-location:21.0.1")

    implementation("com.google.accompanist:accompanist-systemuicontroller:0.34.0")

    implementation("io.coil-kt:coil-compose:2.6.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    implementation("androidx.compose.material:material-icons-extended")

    // Supabase
    implementation(platform("io.github.jan-tennert.supabase:bom:2.3.0"))
    implementation("io.github.jan-tennert.supabase:storage-kt")
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    //implementation("io.github.jan-tennert.supabase:auth-kt")

    // Ktor engine (ОБЯЗАТЕЛЬНО)
    implementation("io.ktor:ktor-client-android:2.3.7")

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}