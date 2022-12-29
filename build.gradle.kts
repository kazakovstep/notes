plugins {
    java
    id("org.springframework.boot") version "3.0.0"
    id("io.spring.dependency-management") version "1.1.0"
}

group = "edu.kazakov"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("net.bytebuddy:byte-buddy")

    implementation("org.springframework.security:spring-security-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.security:spring-security-config")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation ("org.projectlombok:lombok")
    annotationProcessor ("org.projectlombok:lombok")
}


tasks.withType<Test> {
    useJUnitPlatform()
}
