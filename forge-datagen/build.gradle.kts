val modId: String = "compactmachines"

plugins {
    id("java")
    id("maven-publish")
    alias(neoforged.plugins.mdg.legacy)
}

val apiProject = project(":forge-api")
val mainProject = project(":forge-main")
evaluationDependsOn(mainProject.path)

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

    mods.create(modId) {
        modSourceSets.add(mainProject.sourceSets.main)
        modSourceSets.add(sourceSets.main)
    }

    runs {
        create("data") {
            this.data()

            this.gameDirectory.set(file("runs/data"))

            programArguments.addAll("--mod", modId)
            programArguments.addAll("--all")
            programArguments.addAll("--output", mainProject.file("src/generated/resources").absolutePath)
            programArguments.addAll("--existing", mainProject.file("src/main/resources").absolutePath)
        }
    }
}

repositories {
    mavenLocal()
    // location of the maven that hosts JEI files
    maven("https://maven.blamejared.com/") {
        // location of the maven that hosts JEI files since January 2023
        name = "Jared's maven"
    }

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
    compileOnly(project(":forge-api"))
    implementation(mainProject)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-proc:none")
    options.compilerArgs.addAll(arrayOf("-Xmaxerrs", "9000"))
}
