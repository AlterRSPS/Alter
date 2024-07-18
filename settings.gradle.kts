rootProject.name = "Alter"

plugins {
    id("de.fayard.refreshVersions") version ("0.51.0")
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":util")
include(":game-plugins")
include(":game-api")
include(":game-server")
include(":data-builder")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            files("../gradle/libs.versions.toml")
        }
    }
}
include("data-builder")
