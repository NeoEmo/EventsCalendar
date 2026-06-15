plugins {
    id("checkstyle")
    id("application")
    id("org.sonarqube") version "7.3.1.8318"
    id("jacoco")
}

group = "hexlet.pet"
version = "0.5-SNAPSHOT"

application {
    mainClass = "hexlet.pet.App"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Source: https://mvnrepository.com/artifact/com.github.ben-manes/gradle-versions-plugin
    runtimeOnly("com.github.ben-manes:gradle-versions-plugin:0.11.1")

    // Source: https://mvnrepository.com/artifact/info.picocli/picocli
    implementation("info.picocli:picocli:4.7.7")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")
}

tasks.test {
    useJUnitPlatform()
}

checkstyle {
    toolVersion = "10.12.4"
}

tasks.getByName("run", JavaExec::class) {
    standardInput = System.`in`
}

sonar {
    properties {
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.projectKey", "NeoEmo_EventsCalendar")
        property("sonar.organization", "neoemo")
        property("sonar.coverage.jacoco.xmlReportPaths", "${layout.buildDirectory.get()}/reports/jacoco/test/jacocoTestReport.xml")
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
    }
}