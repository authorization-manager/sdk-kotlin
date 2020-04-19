plugins {
    kotlin("jvm") version "1.3.72"
    `java-library`
    jacoco
    id("org.sonarqube") version "2.8"
}

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
// JaCoCo configuration -- END

// SonarQube configuration -- BEGIN
sonarqube {
    properties {
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.organization", "beforeigners")
        property("sonar.projectKey", "beforeigners_authorization-manager-sdk-kotlin")
        property("sonar.sources", "src/main/kotlin")
        property("sonar.tests", "src/test/kotlin")
        property("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/jacoco/test/jacocoTestReport.xml")
    }
}

tasks.sonarqube {
    dependsOn(tasks.jacocoTestReport)
}
// SonarQube configuration -- END

repositories {
    jcenter()
}

dependencies {
    implementation(platform(kotlin("bom")))
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}
