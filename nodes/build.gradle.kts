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
}
