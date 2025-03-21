plugins {
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.android.application)
    alias(libs.plugins.ktlint)
}

android {
    namespace = "com.kwasow.musekit"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.kwasow.musekit"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }

        debug {
            isMinifyEnabled = false

            versionNameSuffix = "-beta"
            applicationIdSuffix = ".beta"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    androidResources {
        generateLocaleConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    lint {
        disable.add("MissingTranslation")
    }
}

tasks.withType<Test> {
    testLogging {
        showStandardStreams = true
    }
}

dependencies {
    // BoM
    implementation(platform(libs.koin.bom))

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.core)

    implementation(libs.android.appcompat)
    implementation(libs.android.constraintLayout)
    implementation(libs.android.lifecycle)
    implementation(libs.kotlin.core)
    implementation(libs.material)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.android.test.espresso)
    androidTestImplementation(libs.android.test.junit)
}
