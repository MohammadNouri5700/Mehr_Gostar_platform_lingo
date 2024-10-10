import com.google.protobuf.gradle.id

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
    id("com.google.protobuf")
}
// Sign info : al: key0 pass:Nouri5700
android {


    sourceSets {
        getByName("main") {
            java.srcDirs(
                "build/generated/source/proto/main/grpc",
                "build/generated/source/proto/main/java"
            )
        }
    }

    namespace = "com.android.platform"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.android.platform.germany"
        minSdk = 26
        targetSdk = 34
        versionCode = 3
        versionName = "1.0.3"

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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    System.setProperty("dagger.hilt.disableInstallInCheck", "true")
}

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
//    ksp ("com.google.dagger:hilt-compiler:2.49")


    // Room dependencies
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    kapt(libs.room.compiler)
    implementation(libs.androidx.room.ktx)


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

    implementation(platform(libs.firebase.bom)){
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }
    implementation(libs.firebase.analytics){
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }
    implementation(libs.firebase.crashlytics){
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }
    implementation(libs.firebase.messaging.directboot){
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }
    implementation(libs.shimmer)

    implementation(libs.firebase.messaging){
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }

    implementation(libs.styledcardview)
    implementation(libs.firebase.perf){
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }
    implementation(libs.rootbeer.lib)


//    implementation("com.google.protobuf:protobuf-javalite:4.27.0")


    implementation("io.grpc:grpc-okhttp:1.68.0")
    implementation("io.grpc:grpc-protobuf-lite:1.68.0")
//    implementation("io.grpc:grpc-protobuf:1.68.0")
    implementation("io.grpc:grpc-stub:1.68.0")
    compileOnly("org.apache.tomcat:annotations-api:6.0.53")

}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.28.1"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.68.0"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
            task.plugins {
                id("grpc") {
                    option("lite")
                }
            }
        }
    }
}