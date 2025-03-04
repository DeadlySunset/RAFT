plugins {
    kotlin("jvm") version "2.0.21"
    idea
    alias(libs.plugins.shadow)
    alias(libs.plugins.paper)
    alias(libs.plugins.runpaper)
}

group = project.properties["plugin.group"].toString()
version = project.properties["plugin.version"].toString()

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle(project.properties["paper.version"].toString())
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk9:1.9.0")

}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
        val props = mapOf(
            "name" to project.properties["plugin.name"],
            "version" to project.version,
            "main" to project.properties["plugin.main"],
            "apiVersion" to project.properties["paper.api"],
        )
        inputs.properties(props)
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}

