import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

// The API address lives in gradle.properties, not in the sources: the build
// turns it into a generated ApiConfig object that commonMain compiles against.
val generateApiConfig by tasks.registering {
    val baseUrl = providers.gradleProperty("marketplace.baseUrl")
    val outputDir = layout.buildDirectory.dir("generated/apiConfig/kotlin")
    inputs.property("baseUrl", baseUrl)
    outputs.dir(outputDir)
    doLast {
        val file = outputDir.get().file("ru/bokach/plugins/data/remote/ApiConfig.kt").asFile
        file.parentFile.mkdirs()
        file.writeText(
            """
            |package ru.bokach.plugins.data.remote
            |
            |internal object ApiConfig {
            |    const val BASE_URL: String = "${baseUrl.get().trimEnd('/')}"
            |}
            |""".trimMargin(),
        )
    }
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    
    jvm()
    
    js {
        browser()
        binaries.executable()
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }
    
    android {
       namespace = "ru.bokach.plugins.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
    
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.uiTooling)
            implementation(libs.ktor.clientOkhttp)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.clientCio)
        }
        webMain.dependencies {
            implementation(libs.ktor.clientJs)
        }
        iosMain.dependencies {
            implementation(libs.ktor.clientDarwin)
        }
        commonMain {
            kotlin.srcDir(generateApiConfig)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.navigation3.runtime)
            implementation(libs.navigation3.ui)
            implementation(libs.kotlinx.coroutinesCore)
            implementation(libs.kotlinx.serializationJson)
            implementation(libs.ktor.clientCore)
        }
        jsMain.dependencies {
            implementation(libs.wrappers.browser)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    metricsDestination = layout.buildDirectory.dir("compose_compiler")
}

compose.resources {
    publicResClass = true
    packageOfResClass = "ru.bokach.plugins.resources"
}
