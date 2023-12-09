import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.8.20"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project("common"))
    implementation(project("network"))
    implementation(project(":calculation"))
    implementation(project(":nodes"))

    implementation(compose.desktop.currentOs)
    implementation("org.kodein.di:kodein-di:7.19.0")
    implementation("org.kodein.di:kodein-di-framework-compose:7.19.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    val voyagerVersion = "1.0.0-rc10"
    implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-kodein:$voyagerVersion")

    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("app.cash.turbine:turbine:1.0.0")
}

compose.desktop {
    application {
        mainClass = "org/example/sha1PeerToPeer/MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Sha1PeerToPeerResolver"
            packageVersion = "1.0.0"
        }
    }
}
