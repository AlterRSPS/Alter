import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlin.serialization)
    //alias(libs.plugins.ktlint)
}
allprojects {
    apply(plugin = "idea")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    //apply(plugin = "org.jlleitschuh.gradle.ktlint")
    group = "org.alter"
    version = "0.0.5"

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://repo.maven.apache.org/maven2")
        maven("https://jitpack.io")
        maven("https://raw.githubusercontent.com/OpenRune/hosting/master")
    }

    val lib = rootProject.project.libs
    dependencies {
        implementation(lib.kotlin.logging)
        implementation(lib.logback.classic)
        implementation(lib.fastutil)
        implementation(lib.spark.core)
        implementation(lib.kotlin.stdlib.jdk8)
        implementation(lib.jackson.dataformat.yaml)
        implementation(lib.jackson.dataformat.toml)
        implementation(lib.jackson.databind)
        implementation(lib.json)
        implementation(lib.jbcrypt)
        implementation(lib.gson)
        implementation(lib.netty.all)
        implementation(lib.kotlinx.serialization.core)
        implementation(lib.rsprot)
        implementation(lib.pathfinder)
        if (name != "plugins") {
            implementation(rootProject.projects.plugins.filestore)
        }
        testImplementation(lib.junit)
        testImplementation(lib.kotlin.test.junit)
    }

    tasks.withType<KotlinCompile>().all {
        kotlinOptions {
            languageVersion = "1.7"
            freeCompilerArgs =
                listOf(
                    "-Xallow-any-scripts-in-source-roots",
                )
        }
    }
}

