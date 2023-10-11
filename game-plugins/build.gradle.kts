description = "Alter Servers Plugins"

dependencies {
    implementation(projects.gameServer)
    implementation(projects.util)
    implementation(projects.net)
    implementation(kotlin("script-runtime"))
    implementation(project(":game-api"))
}

tasks.named<Jar>("jar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
