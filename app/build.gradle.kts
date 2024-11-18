import com.google.protobuf.gradle.*

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
    id("com.google.protobuf")
}

// Sign info : al: key0 pass:Nouri5700
android {



    signingConfigs {
        create("Release") {
            storeFile = file("/home/leo/AndroidStudioProjects/Platform/key/debug.jks")
            storePassword = "Nouri5700"
            keyAlias = "key0"
            keyPassword = "Nouri5700"
        }
    }

    namespace = "com.android.platform"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.android.platform.germany"
        minSdk = 26
        targetSdk = 34
        versionCode = 7
        versionName = "1.0.8"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            isMinifyEnabled =false
            isShrinkResources= false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("Release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    System.setProperty("dagger.hilt.disableInstallInCheck", "true")
}
val grpcKotlinVersion = "1.0.0" // https://github.com/grpc/grpc-kotlin/releases
val grpcVersion = "1.35.0" // https://github.com/grpc/grpc-java/releases
val kotlin_version = "1.8.10"
val protobufVersion = "3.15.1" // https://github.com/protocolbuffers/protobuf/releases
val protobufGradlePluginVersion = "0.9.4" //https://github.com/google/protobuf-gradle-plugin


dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.work.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Dagger 2 dependencies
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)
    implementation(libs.dagger)
    implementation(libs.dagger.android)
    implementation(libs.dagger.android.support)

    // Room dependencies
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    kapt(libs.room.compiler)
    implementation(libs.androidx.room.ktx)
//    implementation ("androidx.room:room-runtime:2.4.3")
//    kapt ("androidx.room:room-compiler:2.4.3")
//    implementation ("androidx.room:room-ktx:2.4.3")


    // Kotlin Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // Lifecycle components for ViewModel and LiveData
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.runtime.ktx)

    // Retrofit for network calls (optional)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging.interceptor)

    // Responsive UI
    implementation(libs.sdp.android)

    // Firebase dependencies
    implementation(platform(libs.firebase.bom)) {
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }
    implementation(libs.firebase.analytics) {
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }
    implementation(libs.firebase.crashlytics) {
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }
    implementation(libs.firebase.messaging.directboot) {
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }
    implementation(libs.shimmer)

    implementation(libs.firebase.messaging) {
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }

    implementation(libs.styledcardview)
    implementation(libs.firebase.perf) {
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }
    implementation(libs.rootbeer.lib)


    // Protobuf
    implementation (libs.protobuf.javalite)

    // gRPC
    implementation (libs.grpc.kotlin.stub.lite)
    implementation (libs.grpc.okhttp)
    implementation (libs.javax.annotation.api)

    // GSON
    implementation ("com.google.code.gson:gson:2.11.0")


    implementation ("com.airbnb.android:lottie:6.6.0")

    implementation ("com.github.bumptech.glide:glide:4.16.0")


    implementation ("com.google.android.exoplayer:exoplayer:2.18.1")

    implementation ("com.google.android.flexbox:flexbox:3.0.0")

    implementation  ("com.github.massoudss:waveformSeekBar:5.0.2")

}
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk7@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.builtins {
                id("java") {
                    option("lite")
                }
            }
            it.plugins {
                id("grpc") {
                    option("lite")
                }
                id("grpckt") {
                    option("lite")
                }
            }
        }
    }
}
