plugins {
    kotlin("jvm") version "1.3.72"
    `java-library`
    jacoco
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
}
// JaCoCo configuration -- END

repositories {
    jcenter()
}

dependencies {
    implementation(platform(kotlin("bom")))
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}
