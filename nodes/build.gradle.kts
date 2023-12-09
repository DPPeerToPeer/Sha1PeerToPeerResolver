plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(project(":common"))
    implementation(project(":network"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.kodein.di:kodein-di:7.19.0")
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
}
