import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    idea
}

allprojects {
    apply(plugin = "idea")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = "Alter"
    version = "0.0.4"

    repositories {
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://repo.maven.apache.org/maven2") }
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://repo.runelite.net/") }
    }

    val lib = rootProject.project.libs
    dependencies {
        implementation(lib.fastutil)
        implementation(lib.kotlin-stdlib)
        implementation(lib.log4j)
        implementation(lib.kotlin-logging)
        implementation(lib.jackson)
        implementation(lib.jackson-dataformat-yaml)
        implementation(lib.jackson-dataformat-toml)
        implementation(lib.jackson-module)
        implementation(lib.json)
        implementation(lib.jbcrypt)
        implementation(lib.gson)
        implementation(lib.RLCache)
        implementation(lib.netty-all)
        implementation(lib.commons)
        implementation(lib.spark)
        testImplementation(lib.kotlin)
        testImplementation(lib.junit)
    }
m
    idea {
        module {
            inheritOutputDirs = false
            outputDir = file("${project.buildDir}/classes/kotlin/main")
            testOutputDir = file("${project.buildDir}/classes/kotlin/test")
        }
    }

    tasks.withType(KotlinCompile).configureEach {
        kotlinOptions {
            jvmTarget = jvmVersion
            languageVersion = 1.8
            kotlinOptions.freeCompilerArgs += "-Xallow-any-scripts-in-source-roots"
        }
    }


}

tasks.register<Zip>("packageServer") {
    archiveFileName.set("game-${project.version}.zip")
    destinationDirectory.set(file("."))
    from("./") {
        include("gradlew")
        include("gradlew.bat")
        include("build.gradle.kts")
        include("settings.gradle.kts")
        include("LICENSE")
        include("README.md")
        include("first-launch-template")
        rename ("first-launch-template", "first-launch")
    }
    from("gradle/") {
        into("gradle")
    }
    from("data/") {
        into("data")
        exclude("cache")
        exclude("rsa")
        exclude("saves")
        exclude("xteas")
        exclude("xteas.json")
    }
    from("game-api/") {
        into("/game-api")
        include("src/main/kotlin/org/alter/api/**")
    }
    from("game-server/") {
        into("game-server")
        exclude("build")
        exclude("out")
        exclude("src/main/java")
        exclude("src/test/java")
    }

    from("/game-plugins") {
        into("/game-plugins")
        exclude("build")
        exclude("out")
        exclude("src/main/java")
        exclude("src/test/java")
        include ("src/main/kotlin/org/alter/plugins/content/**")
        include ("src/main/kotlin/org/alter/service/**")
    }
    from("net/") {
        into("net")
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

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        languageVersion = "2.0"
        jvmTarget = "11"


        freeCompilerArgs = listOf(
            "-Xallow-any-scripts-in-source-roots",
            "-Xjvm-default=all",
            "-Xbackend-threads=4"
        )
    }
}
