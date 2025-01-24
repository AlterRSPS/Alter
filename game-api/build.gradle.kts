import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

description = "Alter game-api used by Plugins and Server"

plugins {
    id("org.jetbrains.dokka") version "0.9.18"
}

dependencies {
    implementation(project(":game-server"))
    implementation(project(":util"))
    implementation(rootProject.project.libs.rsprot)
    implementation(rootProject.projects.plugins.filestore)
    implementation(rootProject.projects.plugins.rscm)
}
tasks.withType<DokkaTask> {
    for (pkg in listOf("org.alter.api.cfg")) {
        packageOptions {
            prefix = pkg
            suppress = true
        }
    }
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.compilerOptions {
    languageVersion.set(KotlinVersion.KOTLIN_2_0)
}