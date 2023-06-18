import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("org.jetbrains.dokka") version "0.9.18"
}

dependencies {
    implementation(project(":game-server"))
    implementation(project(":util"))
}

tasks.withType<DokkaTask> {
    for (pkg in listOf("gg.rsmod.plugins.api.cfg")) {
        packageOptions {
            prefix = pkg
            suppress = true
        }
    }
}