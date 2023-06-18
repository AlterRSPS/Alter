description = "Alter Servers Plugins"

dependencies {
    implementation(projects.gameApi)
    implementation(projects.gameServer)
    implementation(projects.util)
    implementation(projects.net)
    implementation(kotlin("script-runtime"))
}

tasks.named<Jar>("jar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
