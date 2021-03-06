import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.6.0"
    id("com.github.johnrengelman.shadow") version "7.1.0"
    application
}

group = "god.sentix"
version = "4.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("'https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")
    compileOnly("com.github.NeumimTo:Pl3xMap:master-SNAPSHOT")
    implementation("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")
    implementation("org.bstats:bstats-bukkit:2.2.1")
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "god.sentix.pl3xmarker.MainKt"
    }

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.withType<ShadowJar> {
    project.configurations.implementation.get().isCanBeResolved = true

    configurations = listOf(project.configurations.runtimeClasspath.get())
    dependencies { exclude { it.moduleGroup != "org.bstats" } }
    relocate("org.bstats", group)
}

application {
    mainClass.set("MainKt")
}