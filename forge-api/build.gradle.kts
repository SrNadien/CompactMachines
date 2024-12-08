import java.text.SimpleDateFormat
import java.util.*

val versionMain: String = System.getenv("VERSION") ?: "9.9.9"

plugins {
    id("java-library")
    id("maven-publish")
    alias(neoforged.plugins.mdg.legacy)
}

sourceSets {
    named("test") {
        resources {
            //The test module has no resources
            setSrcDirs(emptyList<String>())
        }
    }
}

base {
    group = "dev.compactmods.compactmachines"
    version = versionMain
}

java {
    // toolchain.vendor.set(JvmVendorSpec.JETBRAINS)
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    withJavadocJar()
    withSourcesJar()
}

neoForge {
    version = "1.20.1-47.3.0"

    parchment {
        enabled = true
        mappingsVersion = libs.versions.parchment
        minecraftVersion = libs.versions.parchmentMC
    }
}

repositories {
    mavenLocal()
    maven("https://maven.pkg.github.com/compactmods/compactmachines") {
        name = "Github PKG"
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.0")
//    compileOnly(compactmods.spatial)
}



tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-proc:none")
    options.compilerArgs.addAll(arrayOf("-Xmaxerrs", "9000"))
}

tasks.withType<Jar> {
    val gitVersion = providers.exec {
        commandLine("git", "rev-parse", "HEAD")
    }.standardOutput.asText.get()

    manifest {
        val now = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date())
        attributes(
            mapOf(
                "Automatic-Module-Name" to "compactmachines.api",
                "Specification-Title" to "Compact Machines - Core API",
                "Specification-Version" to "1", // We are version 1 of ourselves
                "Implementation-Title" to "Compact Machines - Core API",
                "Implementation-Timestamp" to now,
                "FMLModType" to "GAMELIBRARY",
                "Minecraft-Version" to mojang.versions.minecraft.get(),
                "Forge-Version" to forge.versions.forge.get(),
                "Main-Commit" to gitVersion
            )
        )
    }
}

val PACKAGES_URL = System.getenv("GH_PKG_URL") ?: "https://maven.pkg.github.com/compactmods/compactmachines"
publishing {
    publications.register<MavenPublication>("api") {
        artifactId = "forge-api"
        from(components.getByName("java"))
    }

    repositories {
        // GitHub Packages
        maven(PACKAGES_URL) {
            name = "GitHubPackages"
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}