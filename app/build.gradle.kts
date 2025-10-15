plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)

    alias(libs.plugins.android.application)
    alias(libs.plugins.android.baselineprofile)
    alias(libs.plugins.google.protobuf)
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
        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")

            isDebuggable = false
            isProfileable = true
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
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
        compose = true
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

    ktlint {
        filter {
            exclude { element ->
                element.file.name == "rememberBoundService.kt"
            }
        }
    }

    baselineProfile {
        mergeIntoMain = true
    }
}

tasks.withType<Test> {
    testLogging {
        showStandardStreams = true
    }
}

dependencies {
    // BoM
    implementation(platform(libs.compose.bom))
    implementation(platform(libs.koin.bom))

    // Compose
    implementation(libs.compose.accompanist.permissions)
    implementation(libs.compose.livedata)
    implementation(libs.compose.material)
    implementation(libs.compose.material3)
    implementation(libs.compose.material3.adaptive)
    implementation(libs.compose.material3.adaptive.layout)
    implementation(libs.compose.material3.adaptive.navigation)
    implementation(libs.compose.navigation)
    implementation(libs.compose.ui.tooling.preview)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.koin.compose.base)

    // Other
    implementation(libs.android.appcompat)
    implementation(libs.android.dataStore)
    implementation(libs.android.graphics.shapes)
    implementation(libs.android.lifecycle)
    implementation(libs.android.profileInstaller)
    implementation(libs.google.libraries.protobuf)
    implementation(libs.kotlin.core)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.android.test.espresso)
    androidTestImplementation(libs.android.test.junit)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.tooling.base)
    debugImplementation(libs.compose.ui.test.manifest)

    baselineProfile(project(":app:baselineprofile"))
}

protobuf {
    protoc {
        artifact = libs.google.libraries.protobuf.compiler.get().toString()
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
                create("kotlin") {
                    option("lite")
                }
            }
        }
    }
}
