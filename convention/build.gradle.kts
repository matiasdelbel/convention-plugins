plugins {
    `kotlin-dsl`
}

group = "com.delbel.gradle.plugins"
sourceSets["main"].java.srcDirs("src/main/kotlin-extensions")

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    // Plugins
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")
    implementation("com.android.tools.build:gradle:7.0.4")
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.21")
}

gradlePlugin {
    plugins {
        /* Set up the basic configuration for an android module. */
        register("com.delbel.android.library") {
            id = "com.delbel.android.library"
            implementationClass = "com.delbel.gradle.plugin.api.AndroidLibraryPlugin"
        }
        /* Set up the basic configuration for an android compose module. */
        register("com.delbel.android.library.compose") {
            id = "com.delbel.android.library.compose"
            implementationClass = "com.delbel.gradle.plugin.api.ComposeLibraryPlugin"
        }
    }
}
