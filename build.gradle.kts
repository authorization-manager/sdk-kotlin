plugins {
    kotlin("jvm") version "1.3.72"
    `java-library`
    jacoco
    id("org.sonarqube") version "2.8"
    id("com.diffplug.gradle.spotless") version "3.28.1"
}

// spotless configuration -- BEGIN
spotless {
    kotlin {
        ktlint()
    }
    kotlinGradle {
        ktlint()
    }
}

tasks.spotlessCheck {
    mustRunAfter(tasks.classes, tasks.testClasses)
}

tasks.test {
    mustRunAfter(tasks.spotlessCheck)
}

tasks.check {
    dependsOn(tasks.spotlessCheck)
}
// spotless configuration -- END

// JaCoCo configuration -- BEGIN
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
// JaCoCo configuration -- END

// SonarQube configuration -- BEGIN
sonarqube {
    properties {
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.organization", "beforeigners")
        property("sonar.projectKey", "beforeigners_authorization-manager-sdk-kotlin")
    }
}
// SonarQube configuration -- END

// TEST LOGGING -- BEGIN
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
tasks.withType<Test> {
    useJUnitPlatform()
}
// JUNIT -- END

repositories {
    jcenter()
}

dependencies {
    implementation(platform(kotlin("bom")))
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.google.code.gson:gson:2.8.6")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
    testImplementation("org.assertj:assertj-core:3.15.0")
}
