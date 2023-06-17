import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
}
description = "Alter Game"

application {
    mainClass.set("org.alter.game.Launcher")
}

val lib = rootProject.project.libs
dependencies {
    implementation(project(":util"))
    implementation(project(":net"))
    runtimeOnly(project(":game-plugins"))
    implementation("org.jetbrains.kotlin:kotlin-scripting-common:1.4.21")
    implementation("org.jetbrains.kotlin:kotlin-script-runtime:1.4.21")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.0")
    implementation("org.reflections:reflections:0.9.11")
    implementation("commons-io:commons-io:2.4")
    implementation("io.github.classgraph:classgraph:4.6.12")
    implementation("it.unimi.dsi:fastutil:8.2.1")
    implementation("org.bouncycastle:bcprov-jdk15on:1.54")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
    implementation(lib.jackson.dataformat.yaml)
    testImplementation("junit:junit:0.9.11")
}

sourceSets {
    named("main") {
        kotlin.srcDirs("src/main/kotlin")
        resources.srcDirs("src/main/resources")
    }
}

tasks.register("install") {
    description = "Install Alter"

    doFirst {
        val cacheList = listOf(
            "/cache/main_file_cache.dat2",
            "/cache/main_file_cache.idx0",
            "/cache/main_file_cache.idx1",
            "/cache/main_file_cache.idx2",
            "/cache/main_file_cache.idx3",
            "/cache/main_file_cache.idx4",
            "/cache/main_file_cache.idx5",
            "/cache/main_file_cache.idx7",
            "/cache/main_file_cache.idx8",
            "/cache/main_file_cache.idx9",
            "/cache/main_file_cache.idx10",
            "/cache/main_file_cache.idx11",
            "/cache/main_file_cache.idx12",
            "/cache/main_file_cache.idx13",
            "/cache/main_file_cache.idx14",
            "/cache/main_file_cache.idx15",
            "/cache/main_file_cache.idx16",
            "/cache/main_file_cache.idx17",
            "/cache/main_file_cache.idx18",
            "/cache/main_file_cache.idx19",
            "/cache/main_file_cache.idx20",
            "/cache/main_file_cache.idx255",
            "xteas.json"
        )

        cacheList.forEach {
            val file = File("${rootProject.projectDir}/data/$it")
            if (!file.exists()) {
                throw GradleException("\u001B[45m \u001B[30m Missing file! : $file. Go back to: https://github.com/AlterRSPS/Alter and read how to setup plz >____> It's so easy to set this up and you failed at it wtfff?!?!. \u001B[0m")
            }
        }
    }

    doLast {
    // @TODO
    //
    //        copy {
    //            into "${rootProject.projectDir}/"
    //            from "${rootProject.projectDir}/game.example.yml"
    //            rename 'game.example.yml', 'game.yml'
    //            from "${rootProject.projectDir}/dev-settings.example.yml"
    //            rename 'dev-settings.example.yml', 'dev-settings.yml'
    //            file("${rootProject.projectDir}/first-launch").createNewFile()
    //        }
    //        javaexec {
    //            workingDir = rootProject.projectDir
    //            classpath = sourceSets.main.runtimeClasspath
    //            main = "org.alter.game.service.rsa.RsaService"
    //            args = [ "16", "1024", "./data/rsa/key.pem" ] // radix, bitcount, rsa pem file
    //        }
    //    }
    }
}

task<Copy>("extractDependencies") {
    from(zipTree("build/distributions/game-server-${project.version}.zip")) {
        include("game-${project.version}/lib/*")
        eachFile {
            path = name
        }
        includeEmptyDirs = false
    }
    into("build/deps")
}

task<JavaExec>("ktlint") {
    group = "verification"
    description = "Check Kotlin code style."
    classpath = configurations["ktlint"]
    setMain("com.github.shyiko.ktlint.Main")
    args("src/**/*.kt")
}

tasks.register<Copy>("applicationDistribution") {
    from("$rootDir/data/") {
        into("bin/data/")
        include("**")
        exclude("saves/*")
    }
}

tasks.named<Copy>("applicationDistribution") {
    from("$rootDir") {
        into("bin")
        include("/game-plugins/*")
        include("game.example.yml")
        rename("game.example.yml", "game.yml")
    }
}


tasks.named<Zip>("shadowDistZip") {
    from("$rootDir/data/") {
        into("game-shadow-${project.version}/bin/data/")
        include("**")
        exclude("saves/*")
    }

    from("$rootDir") {
        into("game-shadow-${project.version}/bin/")
        include("/game-plugins/*")
        include("game.example.yml")
        rename("game.example.yml", "game.yml")
    }
}


tasks.register<Tar>("myShadowDistTar") {
    archiveFileName.set("game-shadow-${project.version}.tar")
    destinationDirectory.set(file("build/distributions/"))

    from("$rootDir/data/") {
        into("game-shadow-${project.version}/bin/data/")
        include("**")
        exclude("saves/*")
    }

    from("$rootDir") {
        into("game-shadow-${project.version}/bin/")
        include("/game-plugins/*")
        include("game.example.yml")
        rename("game.example.yml", "game.yml")
    }
}


tasks.named("build") {
    finalizedBy("extractDependencies")
}

tasks.named("install") {
    dependsOn("build")
}

tasks.named<Jar>("jar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks {
    named<ProcessResources>("processResources") {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE // or EXCLUDE, WARN, FAIL
    }
}
