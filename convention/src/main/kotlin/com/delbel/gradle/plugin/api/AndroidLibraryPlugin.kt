package com.delbel.gradle.plugin.api

import org.gradle.api.Plugin
import org.gradle.api.Project
import com.delbel.gradle.plugin.android
import com.delbel.gradle.plugin.build
import com.delbel.gradle.plugin.kotlinOptions
import com.delbel.gradle.plugin.plugins
import org.gradle.api.JavaVersion

@Suppress(names = ["UnstableApiUsage"])
class AndroidLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) = target.build {
        plugins {
            apply("com.android.library")
            apply("org.jetbrains.kotlin.android")
        }

        android {
            compileSdk = 33
            buildToolsVersion = "33.0.0"

            defaultConfig {
                targetSdk = 33
                minSdk = 23
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }

            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_1_8.toString()
                useIR = true
                freeCompilerArgs = freeCompilerArgs.toMutableList() + "-Xopt-in=kotlin.RequiresOptIn" + "-Xjvm-default=all-compatibility"
            }
        }
    }
}
