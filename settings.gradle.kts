import java.nio.file.Files
import java.nio.file.Path

rootProject.name = "Alter"
pluginManagement {
    plugins {
        kotlin("jvm")
    }
}
plugins {
    id("de.fayard.refreshVersions") version ("0.51.0")
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            files("../gradle/libs.versions.toml")
        }
    }
}

include(":util")
include(":game-plugins")
include(":game-api")
include(":game-server")
include(":plugins")

includePlugins(project(":plugins"))
fun includePlugins(pluginProject: ProjectDescriptor) {
    val pluginPath = pluginProject.projectDir.toPath()
    Files.walk(pluginPath).forEach {
        if (!Files.isDirectory(it)) {
            return@forEach
        }
        searchPlugin(pluginProject.name, pluginPath, it)
    }
}

fun searchPlugin(parentName: String, pluginRoot: Path, currentPath: Path) {
    val hasBuildFile = Files.exists(currentPath.resolve("build.gradle.kts"))
    if (!hasBuildFile) {
        return
    }
    val relativePath = pluginRoot.relativize(currentPath)
    val pluginName = relativePath.toString().replace(File.separator, ":")
    include("$parentName:$pluginName")
}
