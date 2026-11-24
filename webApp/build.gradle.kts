import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared"))

            implementation(libs.compose.ui)
        }
    }
}

val workerBundle = project(":shared").layout.buildDirectory.dir("kotlin-webpack/js/productionExecutable")
tasks.withType<ProcessResources>().matching {
    it.name == "jsProcessResources" || it.name == "wasmJsProcessResources"
}.configureEach {
    dependsOn(":shared:jsBrowserProductionWebpack")
    from(workerBundle) {
        into("article-worker")
        include("*.js")
    }
}
