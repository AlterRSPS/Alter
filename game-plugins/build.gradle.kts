description = "Alter Servers Plugins"

dependencies {
    implementation(project(":game-api"))
    implementation(project(":game-server"))
    implementation(project(":util"))
    implementation(project(":net"))
    implementation(kotlin("script-runtime"))
}

tasks.named<Jar>("jar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}