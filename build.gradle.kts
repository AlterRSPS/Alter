import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm) apply true
    alias(libs.plugins.kotlin.serialization)
    idea
}

allprojects {
    apply(plugin = "idea")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = "org.alter"
    version = "0.0.5"
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
    kotlin{
        jvmToolchain{
            languageVersion.set(JavaLanguageVersion.of(17))
        }

    }
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://repo.maven.apache.org/maven2")
        maven("https://jitpack.io")
        maven("https://repo.runelite.net/")
        maven("https://raw.githubusercontent.com/MatthewBishop/hosting/main/")
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
        implementation(lib.cache)
        implementation(lib.netty.all)
        implementation(lib.kotlinx.serialization.core)
        testImplementation(lib.junit)
        testImplementation(lib.kotlin.test.junit)

        implementation("net.rsprot:osrs-221-api:1.0-SNAPSHOT")
        implementation("io.netty:netty-buffer:4.0.34.Final")
    }

    idea {
        module {
            inheritOutputDirs = false
            outputDir = file("${project.buildDir}/classes/kotlin/main")
            testOutputDir = file("${project.buildDir}/classes/kotlin/test")
        }
    }

    tasks.compileJava {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
    }

    tasks.withType<KotlinCompile>().all {
        kotlinOptions {
            languageVersion = "1.7"
            jvmTarget = "17"
            freeCompilerArgs = listOf(
                "-Xallow-any-scripts-in-source-roots" ,
            )
        }
    }
}

tasks.register<Zip>("packageServer") {
    archiveFileName.set("Alter.zip")
    destinationDirectory.set(file("."))

    from("./") {
        include("gradlew")
        include("gradlew.bat")

        include("build.gradle.kts")
        include("settings.gradle.kts")

        include("LICENSE")
        include("README.md")

        include("management-interface")

        include("first-launch-template")
        rename("first-launch-template", "first-launch")
    }

    from ("gradle/") {
        into ("gradle")
    }

    from("data/") {
        into("data")
        exclude("cache")
        exclude("rsa")
        exclude("saves")
        exclude("xteas")
        exclude("xteas.json")
    }

    from("game-server/") {
        into("game-server/")

        exclude("build")
        exclude("out")
        exclude("plugins")
        exclude("src/main/java")
        exclude("src/test/java")


    }

    from("game-plugins/") {
        into("game-plugins/")
        include("src/main/kotlin/alter/plugins/**")
        //include("src/main/kotlin/alter/plugins/content/osrs.kts")
    }

    from("game-api/") {
        into("game-api/")
        include("src/main/kotlin/alter/plugins/api/**")
        include("src/main/kotlin/alter/plugins/service/**")
    }

    from("net/") {
        into("net")

        exclude("build")
        exclude("out")
        exclude("src/main/java")
        exclude("src/test/java")
    }

    from("tools/") {
        into("tools")

        exclude("build")
        exclude("out")
        exclude("src/main/java")
        exclude("src/test/java")
    }

    from("util/") {
        into("util")

        exclude("build")
        exclude("out")
        exclude("src/main/java")
        exclude("src/test/java")
    }
}

tasks.register<Zip>("packageLibs") {
    archiveFileName.set("Alter-libs.zip")
    destinationDirectory.set(file("."))

    from("game/build/libs/") {
        rename("game-${project.version}.jar", "game.jar")
    }

    from("game/plugins/build/libs/") {
        rename("plugins-${project.version}.jar", "plugins.jar")
    }
}


