description = "Alter Servers Plugins"
val lib = rootProject.project.libs

dependencies {
    implementation(projects.gameServer)
    implementation(projects.util)
    implementation(project(":game-api"))
    implementation(rootProject.project.libs.rsprot)
    implementation(rootProject.projects.plugins.filestore)
    implementation(rootProject.projects.plugins.rscm)
    implementation(lib.routefinder)
}

tasks.named<Jar>("jar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}