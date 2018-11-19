plugins {
    kotlin("jvm") version ("1.3.0")
}

buildscript {

    repositories {
        gradlePluginPortal()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", "1.3.0"))
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8", "1.3.0"))
}

repositories {
    gradlePluginPortal()
}