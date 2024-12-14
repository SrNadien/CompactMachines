rootProject.name = "Compact Machines"

dependencyResolutionManagement {
    addVersionCatalog(this, "mojang")
    addVersionCatalog(this, "forge")
    addVersionCatalog(this, "compactmods")
    versionCatalogs {
        create("neoforged") {
            plugin("mdg-legacy", "net.neoforged.moddev.legacyforge")
                .version("2.0.56-beta")
        }
    }
}

pluginManagement {
    plugins {
        id("idea")
        id("eclipse")
        id("maven-publish")
    }

    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()

        maven("https://maven.minecraftforge.net")

        maven("https://maven.parchmentmc.org") {
            name = "ParchmentMC"
        }

        maven("https://maven.neoforged.net/releases") {
            name = "NeoForged"
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include("forge-api")
include("forge-main")

fun addVersionCatalog(dependencyResolutionManagement: DependencyResolutionManagement, name: String) {
    dependencyResolutionManagement.versionCatalogs.create(name) {
        from(files("./gradle/$name.versions.toml"))
    }
}
include("forge-datagen")
