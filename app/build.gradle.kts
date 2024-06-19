plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "project.hansungcomputerdepartment"
    compileSdk = 34

    defaultConfig {
        applicationId = "project.hansungcomputerdepartment"
        minSdk = 23
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

    android {
        namespace = "project.hansungcomputerdepartment"
        compileSdk = 34

        defaultConfig {
            applicationId = "project.hansungcomputerdepartment"
            minSdk = 23
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
        implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
        implementation("com.google.firebase:firebase-analytics")
        implementation("com.google.firebase:firebase-auth")
        implementation("com.google.firebase:firebase-firestore:24.0.0")
        implementation("androidx.viewpager2:viewpager2:1.1.0")
        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.recyclerview:recyclerview:1.2.1")
        implementation("androidx.appcompat:appcompat:1.3.1")
        implementation("androidx.fragment:fragment-ktx:1.3.6")
        implementation("androidx.viewpager:viewpager:1.0.0")
        implementation ("com.google.firebase:firebase-database:20.2.2")
        implementation ("com.google.android.material:material:1.8.0")
        implementation ("com.google.android.gms:play-services-maps:18.0.2")
        implementation ("com.google.android.gms:play-services-location:21.0.1")
        //implementation ("com.android.support:design:28.0.0")
        //implementation("com.prolificinteractive:material-calendarview:1.4.3")

        // Remove the following line if it exists in your build.gradle
        // implementation("com.android.support:support-compat:25.1.1")

        // Remove any redundant or old versions of dependencies
        // Ensure you have only one version of each dependency
        implementation(libs.appcompat)
        implementation(libs.material)
        implementation(libs.activity)
        implementation(libs.constraintlayout)

        testImplementation(libs.junit)
        androidTestImplementation(libs.ext.junit)
        androidTestImplementation(libs.espresso.core)
    }


