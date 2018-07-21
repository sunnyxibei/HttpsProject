plugins {
    kotlin("jvm") version ("1.2.51")
}

buildscript {

    repositories {
        gradlePluginPortal()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", "1.2.51"))
    }
}

dependencies {
    implementation(gradleKotlinDsl())
    implementation(kotlin("stdlib-jdk8", "1.2.51"))
}

repositories {
    gradlePluginPortal()
}