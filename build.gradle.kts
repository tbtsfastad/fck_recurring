import org.gradle.api.publish.maven.MavenPublication

plugins {
    id("com.android.library") version "9.1.1"
    id("com.google.devtools.ksp") version "2.3.12"
    `maven-publish`
}

group = "com.github.tbtsfastad"
version = "1.0.1"

android {
    namespace = "fck.and.recurring"
    compileSdk = 36
    defaultConfig {
        minSdk = 26
        consumerProguardFiles("consumer-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation("androidx.work:work-runtime:2.11.2")
    implementation("androidx.room:room-runtime:2.8.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    ksp("androidx.room:room-compiler:2.8.5")
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                from(components["release"])
                artifactId = "fck_recurring"
            }
        }
    }
}
