plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.adryxta.octoview.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {

    api(libs.converter.kotlinx.serialization)
    api(libs.kotlinx.serialization.json)
    api(libs.kotlinx.coroutines.android)

    implementation(libs.retrofit)



    testImplementation(libs.junit)
    testImplementation(libs.mockk.android)
    testImplementation(libs.mockk.agent)
    testImplementation(libs.turbine)
    testImplementation(libs.truth)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}