# Convention Plugins
This repository defines a set of convention plugins, used to keep a single source of truth for 
common module configurations. It defines a set of plugins that all normal modules can use to 
configure themselves.

This approach is heavily based on [square's herding elephants](https://developer.squareup.com/blog/herding-elephants/) 
and [@jjohannes' idiomatic-gradle](https://github.com/jjohannes/idiomatic-gradle).

With this approach, we can avoid duplicated build script setup, messy subproject configurations,
without the pitfalls of the buildSrc directory. `convention-plugins` is an included build, as 
configured in the root `settings.gradle.kts`.

## How to use it?
### Adding the plugins to an existing repository
1. Add the submodule to your repository:
```shell
git submodule add -b release/1.0.0-alpha01 git@github.com:matiasdelbel/convention-plugins.git
git submodule init
```

This will generate a `.gitmodules` file:
```shell
[submodule "convention-plugins"]
	path = convention-plugins
	url = git@github.com:matiasdelbel/convention-plugins.git
	branch = release/1.0.0-alpha01
```

2. Fetch the latest changes (it shouldn't be needed because the branch should contain only the release commit).
```shell
# update your submodule --remote fetches new commits in the submodules
# and updates the working tree to the commit described by the branch
git submodule update --remote
```

3. Update the `settings.gradle` file so the plugin management includes the defined plugins.
```kotlin
pluginManagement {
    includeBuild("convention-plugins")
    // Other configurations
}
```

4. Check in the `convention-plugins/build.gradle.kts` file the available plugins. Add the one needed 
to your module.
```groovy
plugins {
    id 'com.delbel.android.library'
    // Other plugins...
}
```

## Available plugins
### `com.delbel.android.library`
#### What it does?
Set up the basic configuration for an android module for you:
```groovy
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

        freeCompilerArgs = freeCompilerArgs.toMutableList() + "-Xopt-in=kotlin.RequiresOptIn" + "-Xjvm-default=all-compatibility"
    }
}
```

#### Set up
Apply the plugin:
```groovy
plugins {
    id 'com.delbel.android.library'
    // Other plugins...
}
```

### `com.delbel.android.library.compose`
#### What it does?
Set up the basic configuration for an android compose module for you:
```kotlin
project.build {
    val versionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

    buildFeatures { compose = true }

    composeOptions {
        kotlinCompilerExtensionVersion = versionCatalog
                .findVersion("androidxCompose")
                .get()
                .toString()

        kotlinCompilerVersion = versionCatalog
                .findVersion("androidxComposeCompilerVersion")
                .get()
                .toString()
    }

    dependencies {
        add("debugImplementation", versionCatalog.findDependency("androidxComposeUiTooling").get())

        add("implementation", versionCatalog.findDependency("androidxComposeMaterial").get())
        add("implementation", versionCatalog.findDependency("androidxComposeUi").get())
    }
}
```

#### Set up
Apply the plugin:
```groovy
plugins {
    id 'com.delbel.android.library.compose'
    // Other plugins...
}
```

The `versionCatalogs` is being uses for providing the versions, (see 
[sharing dependency versions between projects](https://docs.gradle.org/current/userguide/platforms.html)).

In order to provide the versions, you will need to create a `../gradle/libs.versions.toml` file and 
add the following content:
```markdown
[versions]
androidxCompose = "1.1.1"
androidxComposeCompilerVersion = "1.6.10"

[libraries]
androidxComposeMaterial = { group = "androidx.compose.material", name = "material", version.ref = "androidxCompose" }
androidxComposeUi = { group = "androidx.compose.ui", name = "ui", version.ref = "androidxCompose" }
androidxComposeUiTooling = { group = "androidx.compose.ui", name = "ui-tooling", version.ref = "androidxCompose" }
```

## Contributing
Being the plugins a git submodule makes it super easy to contribute. Let's say that you found a bug 
on one of the plugins, you can apply the fix directly in your project workspace, test it, and then 
push your changes into the plugin's repository. 

```shell
# update submodule in a working branch
cd convention-plugins

git checkout -b your-branch-name
git add . 
git commit -m "Your commit message"
git push --set-upstream origin your-branch-name

cd ..
```

If your want to use your version of the fixed plugin until a new version is being release you first
need to update the `.gitmodule` file.
```shell
[submodule "convention-plugins"]
	path = convention-plugins
	url = git@github.com:matiasdelbel/convention-plugins.git
	branch = your-branch-name

```

Finally, you have to update your main repository with the changes.
```shell
# commit the change in main repo
# to use the latest commit in master of the submodule
# share your changes
git add convention-plugins
git submodule update --remote
git add .gitmodules
git commit -m "Switching to custom version until the new release of it."
git push
```

## Further reading
- [Working with submodules](https://www.vogella.com/tutorials/GitSubmodules/article.html)
- [Convention plugins on NowAndroidApp](https://github.com/android/nowinandroid/tree/main/build-logic)
  