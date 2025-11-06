plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    
    // MySQL driver
    runtimeOnly("com.mysql:mysql-connector-j")
    
    // Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    
    // Security (optional - uncomment if needed)
    // implementation("org.springframework.boot:spring-boot-starter-security")
    
    // Validation
    implementation("org.springframework.boot:spring-boot-starter-validation")
    
    // Dev tools for hot reload
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}