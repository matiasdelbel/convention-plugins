package com.delbel.gradle.plugin.api

import com.android.build.api.dsl.BuildFeatures
import com.android.build.api.dsl.ComposeOptions
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.delbel.gradle.plugin.build
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

@Suppress(names = ["UnstableApiUsage"])
class ComposeLibraryPlugin : Plugin<Project> {

    override fun apply(project: Project) = project.build {
        buildFeatures { compose = true }

        composeOptions { versionCatalog ->
            kotlinCompilerExtensionVersion = versionCatalog
                .findVersion("androidxComposeCompiler")
                .get()
                .toString()
        }
    }

    private fun Project.buildFeatures(block: BuildFeatures.() -> Unit) = extensions
        .getByType<LibraryExtension>()
        .apply { buildFeatures{ block() } }

    private fun Project.composeOptions(block: ComposeOptions.(VersionCatalog) -> Unit) {
        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

        extensions
            .getByType<LibraryExtension>()
            .apply { composeOptions { block(libs) } }
    }
}
