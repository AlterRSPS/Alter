plugins {
    alias(libs.plugins.shadow)
    application
    `maven-publish`
}
description = "Alter Game Server Launcher"
application {
    apply(plugin = "maven-publish")
    mainClass.set("org.alter.game.Launcher")
}
val lib = rootProject.project.libs
dependencies {
    with(lib) {
        implementation(projects.util)
        runtimeOnly(projects.gamePlugins)
        implementation(kotlinx.coroutines)
        implementation(reflection)
        implementation(commons)
        implementation(classgraph)
        implementation(fastutil)
        implementation(bouncycastle)
        implementation(jackson.module.kotlin)
        implementation(jackson.dataformat.yaml)
        implementation(kotlin.csv)
        implementation(mongo.bson)
        implementation(mongo.driver)
        implementation(rootProject.projects.plugins.rscm)
        testImplementation(junit)
        implementation(rootProject.project.libs.rsprot)
        implementation(rootProject.projects.plugins.filestore)
        implementation(rootProject.projects.plugins.rscm)
        implementation(rootProject.projects.plugins.tools)
        implementation(lib.routefinder)
        implementation("net.lingala.zip4j:zip4j:2.11.5")
        implementation("me.tongfei:progressbar:0.9.5")
    }
}
sourceSets {
    named("main") {
        kotlin.srcDirs("src/main/kotlin")
        resources.srcDirs("src/main/resources")
    }
}

tasks.register<JavaExec>("downloadCache") {
    description = "Downloads the OSRS cache and XTEA keys based on revision in game.example.yml"
    group = "application"
    workingDir = rootProject.projectDir
    classpath = sourceSets["main"].runtimeClasspath + project(":plugins:tools").sourceSets["main"].runtimeClasspath
    mainClass.set("dev.openrune.cache.tools.CacheDownloader")
    
    // Set environment variables if needed
    environment("DOWNLOAD_DIR", "${rootProject.projectDir}/data")
    environment("CACHE_DIR", "${rootProject.projectDir}/data/cache")
    
    // Pass arguments to the downloader if needed
    args = listOf("--verbose")

    // Ensure stdout and stderr are redirected to the console
    standardOutput = System.out
    errorOutput = System.err
}

tasks.register("downloadOSRSCache") {
    description = "Downloads the latest OSRS cache and XTEA keys"
    group = "other"
    dependsOn("downloadCache")
    
    doLast {
        println("\u001B[32m Cache and XTEA keys have been successfully downloaded! \u001B[0m")
    }
}

tasks.register("checkCache") {
    description = "Checks if the OSRS cache and XTEA keys exist"
    group = "other"
    
    doLast {
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
            "/cache/main_file_cache.idx17",
            "/cache/main_file_cache.idx18",
            "/cache/main_file_cache.idx19",
            "/cache/main_file_cache.idx20",
            "/cache/main_file_cache.idx255",
            "xteas.json",
        )
        
        val missingFiles = cacheList.filter { !File("${rootProject.projectDir}/data/$it").exists() }
        
        if (missingFiles.isEmpty()) {
            println("\u001B[32m All required cache files and XTEA keys are present. \u001B[0m")
        } else {
            println("\u001B[33m Missing required files: ${missingFiles.joinToString(", ")} \u001B[0m")
            println("\u001B[33m Please run the 'downloadOSRSCache' task to download them automatically. \u001B[0m")
        }
    }
}

@Suppress("ktlint:standard:max-line-length")
tasks.register("install") {
    description = "Install Alter"
    val cacheList =
        listOf(
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
            "/cache/main_file_cache.idx17",
            "/cache/main_file_cache.idx18",
            "/cache/main_file_cache.idx19",
            "/cache/main_file_cache.idx20",
            "/cache/main_file_cache.idx255",
            "xteas.json",
        )
    cacheList.forEach {
        val file = File("${rootProject.projectDir}/data/$it")
        if (!file.exists()) {
            throw GradleException(
                "\u001B[45m \u001B[30m Missing file! : $file. Go back to: https://github.com/AlterRSPS/Alter and read how to setup plz >____> It's so easy to set this up and you failed at it wtfff?!?!. \u001B[0m",
            )
        }
    }
    dependsOn("runRsaService")
    dependsOn("decryptMap")

    doLast {
        copy {
            into("${rootProject.projectDir}/")
            from("${rootProject.projectDir}/game.example.yml") {
                rename("game.example.yml", "game.yml")
            }
            from("${rootProject.projectDir}/dev-settings.example.yml") {
                rename("dev-settings.example.yml", "dev-settings.yml")
            }
            file("${rootProject.projectDir}/first-launch").createNewFile()
        }
    }
}
tasks.register<JavaExec>("runRsaService") {
    group = "application"
    workingDir = rootProject.projectDir
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("org.alter.game.service.rsa.RsaService")
    args = listOf("16", "1024", "./data/rsa/key.pem") // radix, bitcount, rsa pem file
}
tasks.register<JavaExec>("decryptMap") {
    description = "Will decrypt world map and remove xteas"
    group = "application"
    workingDir = rootProject.projectDir
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("org.alter.game.service.mapdecrypter.decryptMap")
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
tasks.withType<ProcessResources> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}


/**
 * @TODO Forgot about this one.
 */
publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
        groupId = "org.alter"
        artifactId = "alter"
        pom {
            packaging = "jar"
            name.set("Alter")
            description.set("AlterServer All")
        }
    }
}