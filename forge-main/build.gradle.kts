import org.slf4j.event.Level
import java.text.SimpleDateFormat
import java.util.*

var envVersion = System.getenv("VERSION") ?: "9.9.9"
if (envVersion.startsWith("v"))
    envVersion = envVersion.trimStart('v')

val modId: String = "compactmachines"
val forgeApi = project(":forge-api")

plugins {
    id("java")
    id("maven-publish")
    alias(neoforged.plugins.mdg.legacy)
}

project.evaluationDependsOn(forgeApi.path)

base {
    archivesName = modId
    group = "dev.compactmods.compactmachines"
    version = envVersion
}

java.toolchain.languageVersion = JavaLanguageVersion.of(17)
sourceSets.main {
    resources.srcDir("src/generated/resources")
}

val ssDatagen = sourceSets.create("datagen") {
    java.srcDir("../forge-datagen/src/main/java")
    resources.srcDir("src/main/resources")

    compileClasspath += forgeApi.sourceSets.main.get().output
    compileClasspath += sourceSets.main.get().output

    runtimeClasspath += forgeApi.sourceSets.main.get().output
    runtimeClasspath += sourceSets.main.get().output
}


neoForge {
    version = "1.20.1-47.3.0"

    addModdingDependenciesTo(sourceSets.test.get())
    addModdingDependenciesTo(ssDatagen)

    parchment {
        enabled = true
        mappingsVersion = libs.versions.parchment
        minecraftVersion = libs.versions.parchmentMC
    }

    mods.create(modId) {
        modSourceSets.add(forgeApi.sourceSets.main)
        modSourceSets.add(sourceSets.main)
        modSourceSets.add(ssDatagen)
        modSourceSets.add(sourceSets.test)
    }

    runs {
        // applies to all the run configs below
        configureEach {
            logLevel.set(Level.DEBUG)
            sourceSet = sourceSets.main

            // JetBrains Runtime Hotswap
            if (!System.getenv().containsKey("CI")) {
              jvmArgument("-XX:+AllowEnhancedClassRedefinition")
            }
        }

        create("client") {
            client()
            gameDirectory.set(file("runs/client"))

            // Comma-separated list of namespaces to load gametests from. Empty = all namespaces.
            systemProperty("forge.enabledGameTestNamespaces", modId)

            programArguments.addAll("--username", "Nano")
            programArguments.addAll("--width", "1920")
            programArguments.addAll("--height", "1080")
        }

        create("client2") {
            client()
            gameDirectory.set(file("runs/client"))

            // Comma-separated list of namespaces to load gametests from. Empty = all namespaces.
            systemProperty("forge.enabledGameTestNamespaces", modId)

            programArguments.addAll("--username", "Nano2")
            programArguments.addAll("--width", "1920")
            programArguments.addAll("--height", "1080")
        }

        create("server") {
            server()
            gameDirectory.set(file("runs/server"))

            systemProperty("forge.enabledGameTestNamespaces", modId)
            programArgument("nogui")

            environment.put("CM_TEST_RESOURCES", file("src/test/resources").path)

            sourceSet = sourceSets.test
        }

        create("data") {
            this.data()

            this.gameDirectory.set(file("runs/data"))
            this.sourceSet = ssDatagen

            programArguments.addAll("--mod", modId)
            programArguments.addAll("--all")
            programArguments.addAll("--output", file("src/generated/resources").absolutePath)
            programArguments.addAll("--existing", file("src/main/resources").absolutePath)
        }

        create("gameTestServer") {
            type = "gameTestServer"
            gameDirectory.set(file("runs/gametest"))

            systemProperty("forge.enabledGameTestNamespaces", modId)
            environment.put("CM_TEST_RESOURCES", file("src/test/resources").path)

            sourceSet = sourceSets.test
        }
    }
}

repositories {
    mavenLocal()

    maven("https://www.cursemaven.com") {
        content {
            includeGroup("curse.maven")
        }
    }

    // location of the maven that hosts JEI files
    maven("https://maven.blamejared.com/") {
        // location of the maven that hosts JEI files since January 2023
        name = "Jared's maven"
    }

    // TheOneProbe
    maven("https://maven.k-4u.nl")

    maven("https://maven.tterrag.com/") {
        name = "tterrag maven"
    }

    maven("https://maven.pkg.github.com/compactmods/compactmachines") {
        name = "Github PKG - CompactMods"
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

val USE_JARINJAR_FOR_API = false

dependencies {
    implementation("org.jetbrains:annotations:24.0.0")

    compileOnly(libs.jnanoid)
    testImplementation(libs.jnanoid)
    jarJar(libs.jnanoid)
    additionalRuntimeClasspath(libs.jnanoid)

    compileOnly(forgeApi)
    testCompileOnly(forgeApi)

    if (USE_JARINJAR_FOR_API) {
        jarJar(forgeApi)
    }

    // Gander
    compileOnly(compactmods.bundles.gander)
    additionalRuntimeClasspath(compactmods.bundles.gander)
    jarJar(compactmods.bundles.gander)

    // JEI
    modCompileOnly(libs.jei.commonApi)
    modCompileOnly(libs.jei.forgeApi)
    modRuntimeOnly(libs.jei.forge)

    // Visual Data Mod
    modCompileOnly("curse.maven:theoneprobe-245211:4629624")
    modCompileOnly("curse.maven:jade-324717:5776962")
}

if (!USE_JARINJAR_FOR_API) {
    tasks.named<Jar>("jar") {
        from(forgeApi.sourceSets["main"].output)
        finalizedBy("reobfJar")
    }
}

tasks.withType<Jar> {
    val gitVersion = providers.exec {
        commandLine("git", "rev-parse", "HEAD")
    }.standardOutput.asText.get()

    manifest {
        val now = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date())
        attributes(
            mapOf(
                "Specification-Title" to "Compact Machines",
                "Specification-Vendor" to "CompactMods",
                "Specification-Version" to "2",
                "Implementation-Title" to "Compact Machines",
                "Implementation-Version" to archiveVersion,
                "Implementation-Vendor" to "CompactMods",
                "Implementation-Timestamp" to now,
                "Minecraft-Version" to mojang.versions.minecraft.get(),
                "Forge-Version" to forge.versions.forge.get(),
                "Main-Commit" to gitVersion
            )
        )
    }
}

publishing {
    publications.register<MavenPublication>("release") {
        artifactId = "$modId-forge"
        from(components.getByName("java"))
    }

    repositories {
        // GitHub Packages
        maven("https://maven.pkg.github.com/CompactMods/CompactMachines") {
            name = "GitHubPackages"
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
