package com.delbel.gradle.plugin

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.configure

internal fun Project.build(block: Project.() -> Unit) = block()

internal fun Project.plugins(block: PluginManager.() -> Unit) = pluginManager.apply(block)

internal fun Project.android(block: LibraryExtension.() -> Unit) = extensions.configure<LibraryExtension> { block() }