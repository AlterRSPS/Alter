import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlin.serialization)
}
allprojects {
    apply(plugin = "idea")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    group = "org.alter"
    version = "0.0.5"

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://repo.maven.apache.org/maven2")
        maven("https://jitpack.io")
        maven("https://raw.githubusercontent.com/OpenRune/hosting/master")
        maven("https://repo.openrs2.org/repository/openrs2-snapshots")
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
        testImplementation(lib.junit)
        testImplementation(lib.kotlin.test.junit)
    }

    tasks.withType<KotlinCompile>().all {
        compilerOptions {
            languageVersion.set(KotlinVersion.KOTLIN_2_0)
            freeCompilerArgs = listOf(
                "-Xuse-fir-lt=false"
            )
        }
    }


    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(17)
        }
    }
}

