package com.delbel.gradle.plugin.api

import com.android.build.api.dsl.BuildFeatures
import com.android.build.api.dsl.ComposeOptions
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.delbel.gradle.plugin.build
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

@Suppress(names = ["UnstableApiUsage"])
class ComposeLibraryPlugin : Plugin<Project> {

    override fun apply(project: Project) = project.build {
        val versionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = versionCatalog
                .findVersion("compose")
                .get()
                .toString()

            kotlinCompilerVersion = versionCatalog
                .findVersion("composeCompilerVersion")
                .get()
                .toString()
        }
    }

    private fun Project.buildFeatures(block: BuildFeatures.() -> Unit) = extensions
        .getByType<LibraryExtension>()
        .apply { buildFeatures{ block() } }

    private fun Project.composeOptions(block: ComposeOptions.() -> Unit) {
        extensions
            .getByType<LibraryExtension>()
            .apply { composeOptions { block() } }
    }
}
