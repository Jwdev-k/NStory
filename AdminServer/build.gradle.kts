plugins {
    java
}

group = "nk.service.admin"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation ("org.springframework.boot:spring-boot-starter-actuator:3.3.3")
    implementation ("de.codecentric:spring-boot-admin-starter-server:3.3.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}