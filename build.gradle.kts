import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    val kotlinVersion: String by System.getProperties()
    kotlin("plugin.serialization") version kotlinVersion
    kotlin("multiplatform") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    val kvisionVersion: String by System.getProperties()
    id("io.kvision") version kvisionVersion
    id("io.spring.dependency-management") version System.getProperty("dependencyManagementPluginVersion")
    id("org.springframework.boot") version System.getProperty("springBootVersion")
}

version = "1.3.2"
group = "top.hikki"

task("printVersion") {
    println(project.version)
}

repositories {
    mavenCentral()
    mavenLocal()
}

// Versions
val kotlinVersion: String by System.getProperties()
val kvisionVersion: String by System.getProperties()
val coroutinesVersion: String by project

extra["kotlin.version"] = kotlinVersion
extra["kotlin-coroutines.version"] = coroutinesVersion

val webDir = file("src/jsMain/web")
val mainClassName = "top.hikki.hikkitop.MainKt"

kotlin {
    jvmToolchain(21)
    jvm {
        withJava()
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }
    js(IR) {
        browser {
            commonWebpackConfig {
                outputFileName = "main.bundle.js"
                sourceMaps = false
            }
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
            tasks.getByName("jsProcessResources", Copy::class) {
                duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            }
        }
        binaries.executable()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api("io.kvision:kvision-server-spring-boot:$kvisionVersion")
            }
            kotlin.srcDir("build/generated-src/common")
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation(kotlin("reflect"))
                implementation("org.springframework.boot:spring-boot-starter")
                implementation("org.springframework.boot:spring-boot-devtools")
                implementation("org.springframework.boot:spring-boot-starter-webflux")
                implementation("org.springframework.boot:spring-boot-starter-security")
                implementation("io.ipgeolocation:ipgeolocation:1.0.12")
                implementation("org.apache.commons:commons-lang3:3.0")
                implementation("org.json:json:20240303")
                implementation("org.yaml:snakeyaml:2.3")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation("org.springframework.boot:spring-boot-starter-test")
            }
        }
        val jsMain by getting {
            resources.srcDir(webDir)
            dependencies {
                implementation("io.kvision:kvision:$kvisionVersion")
                implementation("io.kvision:kvision-bootstrap:$kvisionVersion")
                implementation("io.kvision:kvision-state:$kvisionVersion")
                implementation("io.kvision:kvision-fontawesome:$kvisionVersion")
                implementation("io.kvision:kvision-i18n:$kvisionVersion")
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
                implementation("io.kvision:kvision-testutils:$kvisionVersion")
            }
        }
    }
}
