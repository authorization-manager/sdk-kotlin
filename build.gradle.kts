// PLUGINS -- BEGIN
plugins {
    kotlin("jvm") version "1.3.72"
    `java-library`
    jacoco
    id("org.sonarqube") version "2.8"
    id("com.diffplug.gradle.spotless") version "3.28.1"
}

allprojects {
    apply(plugin = "kotlin")
    java.sourceCompatibility = JavaVersion.VERSION_1_8
}
// PLUGINS -- END

// SPOTLESS -- BEGIN
allprojects {
    apply(plugin = "com.diffplug.gradle.spotless")

    spotless {
        kotlin {
            ktlint()
        }
        kotlinGradle {
            ktlint()
        }
    }

    listOf(tasks.compileJava, tasks.compileKotlin, tasks.compileTestJava, tasks.compileTestKotlin).forEach {
        it.get().mustRunAfter(tasks.spotlessCheck)
    }

    tasks.check {
        dependsOn(tasks.spotlessCheck)
    }
}
// SPOTLESS -- END

// SOURCES -- BEGIN
java {
    withSourcesJar()
}
// SOURCES -- END

// JAVADOC -- BEGIN
java {
    withJavadocJar()
}
// JAVADOC -- END

// JACOCO -- BEGIN
allprojects {
    apply(plugin = "jacoco")

    jacoco {
        toolVersion = "0.8.5"
    }

    tasks.jacocoTestReport {
        reports {
            xml.isEnabled = true
            html.isEnabled = true
        }

        dependsOn(tasks.test)
    }

    tasks.build {
        dependsOn(tasks.jacocoTestReport)
    }
}
// JACOCO -- END

// SonarQube -- BEGIN
sonarqube {
    properties {
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.organization", "beforeigners")
        property("sonar.projectKey", "beforeigners_authorization-manager-sdk-kotlin")
    }
}
// SonarQube -- END

// TEST LOGGING -- BEGIN
allprojects {
    tasks.withType<Test> {
        testLogging {
            showStandardStreams = false
            events("skipped", "failed")
            showExceptions = true
            showCauses = true
            showStackTraces = true
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
        afterSuite(printTestResult)
    }
}

val printTestResult: KotlinClosure2<TestDescriptor, TestResult, Void>
    get() = KotlinClosure2({ desc, result ->

        if (desc.parent == null) { // will match the outermost suite
            println("------")
            println(
                    "Results: ${result.resultType} (${result.testCount} tests, ${result.successfulTestCount} " +
                            "successes, ${result.failedTestCount} failures, ${result.skippedTestCount} skipped)"
            )
            println(
                    "Tests took: ${result.endTime - result.startTime} ms."
            )
            println("------")
        }
        null
    })
// TEST LOGGING -- END

// JUNIT -- BEGIN
allprojects {
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
// JUNIT -- END

// Dependencies -- BEGIN
allprojects {
    repositories {
        jcenter()
    }

    dependencies {
        "implementation"(platform(kotlin("bom")))
        "implementation"(kotlin("stdlib-jdk8"))

        "testImplementation"("org.junit.jupiter:junit-jupiter-api:5.6.2")
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:5.6.2")
        "testImplementation"("io.mockk:mockk:1.10.0")
        "testImplementation"("org.assertj:assertj-core:3.15.0")
    }
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.6")
}
// Dependencies -- END
