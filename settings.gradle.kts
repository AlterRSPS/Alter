rootProject.name = "Alter"

plugins {
    id("de.fayard.refreshVersions") version("0.51.0")
}
include(":util")
include(":net")
include(":game-plugins")
include(":game-api")
include(":game-server")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            files("../gradle/libs.versions.toml")
        }
    }
}