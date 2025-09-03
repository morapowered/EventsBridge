import java.util.Objects

plugins {
    java
    `maven-publish`
    alias(libs.plugins.loom)
    alias(libs.plugins.blossom)
    alias(libs.plugins.versioning)
}

version = "${project.property("project_version")}+${project.property("minecraft_version")}"
val isSnapshot = project.findProperty("snapshot")?.toString().toBoolean()
if (isSnapshot) {
    val fixedBranchName = versioning.info.branch.substringAfter("/")
    version = "$version-${fixedBranchName}-${versioning.info.build}"
}

repositories {
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    modImplementation(libs.fabricLoader)
    modImplementation(libs.fabricApi)

    implementation(libs.mbassador)

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

loom {

    serverOnlyMinecraftJar()

    runs {
        remove(runs["client"])
    }

    runConfigs.configureEach {
        ideConfigGenerated(true)
    }
}


java {
    withSourcesJar()
    withJavadocJar()

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

sourceSets {
    main {
        blossom {
            javaSources {
                property("version", project.version.toString())
                property("branch", versioning.info.branch)
                property("build", versioning.info.build)
            }
        }
    }
}

tasks {

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release = 21
    }

    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand("version" to inputs.properties["version"])
        }
    }

    test {
        useJUnitPlatform()
    }
}

publishing {
    publications {
        create<MavenPublication>(project.name) {
            group = project.group
            artifactId = "events-bridge"
            var baseVersion = Objects.requireNonNull(project.property("project_version"), "project needs a version property").toString()
            if (isSnapshot) {
                baseVersion = "$baseVersion-SNAPSHOT"
            }
            version = baseVersion
        }
    }
}
