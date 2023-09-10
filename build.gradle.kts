import org.cyclonedx.gradle.CycloneDxTask
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL

val junitVersion = "5.10.0"

group = "no.jksolbakken"
version = "generatedlater"

plugins {
    kotlin("jvm") version "1.9.10"
    id("org.cyclonedx.bom") version "1.7.4"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

kotlin {
    jvmToolchain(17)
}

tasks {
    withType<Jar> {
        archiveBaseName.set("app")

        manifest {
            attributes["Main-Class"] = "no.nav.MainKt"
            attributes["Class-Path"] = configurations.runtimeClasspath.get().joinToString(separator = " ") {
                it.name
            }
        }

        doLast {
            configurations.runtimeClasspath.get().forEach {
                val file = File("${layout.buildDirectory}/libs/${it.name}")
                if (!file.exists())
                    it.copyTo(file)
            }
        }
    }

    withType<Test> {
        useJUnitPlatform()
        testLogging {
            showExceptions = true
        }
        testLogging {
            exceptionFormat = FULL
        }
    }

    withType<Wrapper> {
        gradleVersion = "8.3"
    }

    withType<CycloneDxTask> {
        setOutputFormat("json")
        setIncludeLicenseText(false)
    }
}
