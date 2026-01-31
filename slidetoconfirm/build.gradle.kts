plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("maven-publish")
}



val androidSourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets["main"].java.srcDirs)
}

val androidJavadocs by tasks.registering(Javadoc::class) {
    isFailOnError = false
    source(android.sourceSets["main"].java.srcDirs)
    classpath += files(
        "${android.sdkDirectory}/platforms/${android.compileSdkVersion}/android.jar"
    )
}

val PUBLISH_GROUP_ID = "com.github.chinalwb"
val PUBLISH_ARTIFACT_ID = "compose-slidetoconfirm"
val PUBLISH_VERSION = "1.0.0"

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = PUBLISH_GROUP_ID
            artifactId = PUBLISH_ARTIFACT_ID
            version = PUBLISH_VERSION

            // Android AAR
            afterEvaluate {
                from(components["release"])
            }

            artifact(androidSourcesJar.get().archiveFile) {
                builtBy(androidSourcesJar)
            }

            pom {
                name.set(PUBLISH_ARTIFACT_ID)
                description.set("Slide to confirm component for Jetpack Compose")
                url.set("https://github.com/chinalwb/slidetoconfirm-compose")

                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
                    }
                }
            }
        }
    }

    repositories {
        maven {
            url = uri("${buildDir}/release/${PUBLISH_VERSION}")
        }
    }
}



android {
    namespace = "com.chinalwb.slidetoconfirm.compose"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 23

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
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
}
