plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.8.20"
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(project(":common"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("io.ktor:ktor-network:2.3.5")
    implementation("org.kodein.di:kodein-di:7.19.0")
}
