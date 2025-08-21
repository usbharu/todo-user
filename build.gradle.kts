plugins {
    kotlin("jvm") version "2.2.10"
    kotlin("plugin.spring") version "2.2.10"
    id("org.springframework.boot") version "3.5.4"
    id("com.gorylenko.gradle-git-properties") version "2.5.2"
    id("com.palantir.git-version") version "4.0.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.graalvm.buildtools.native") version "0.11.0" apply false
    id("org.springdoc.openapi-gradle-plugin") version "1.9.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("org.jetbrains.kotlinx.kover") version "0.9.1"
}

allprojects {
    if (properties.get("enableNativeBuild") == "true") {
        apply {
            plugin("org.graalvm.buildtools.native")
        }
    }
    if (properties.get("useGitVersion") == "false") {
        version = "UNKNWON"
    } else {
        val gitVersion: groovy.lang.Closure<String> by extra
        version = gitVersion()
    }
}

group = "dev.usbharu"


java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.flywaydb:flyway-core")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.8.10")
    developmentOnly("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.10")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:11.11.1")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")

}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

springBoot {
    buildInfo()
}

openApi {
    apiDocsUrl.set("http://localhost:8081/actuator/openapi")
}

configurations.matching { it.name == "detekt" }.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            useVersion(io.gitlab.arturbosch.detekt.getSupportedKotlinVersion())
        }
    }
}

detekt{
    toolVersion = "1.23.8"
    config.setFrom(file("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    autoCorrect = true
    parallel = true
    source = files("src/main/kotlin")
}

dependencyLocking {
    lockAllConfigurations()
}