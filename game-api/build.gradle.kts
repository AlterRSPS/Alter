import org.jetbrains.dokka.gradle.DokkaTask

description = "Alter game-api used by Plugins and Server"

plugins {
    id("org.jetbrains.dokka") version "0.9.18"
}

dependencies {
    implementation(project(":game-server"))
    implementation(project(":util"))
}

tasks.withType<DokkaTask> {
    for (pkg in listOf("org.alter.api.cfg")) {
        packageOptions {
            prefix = pkg
            suppress = true
        }
    }
}
