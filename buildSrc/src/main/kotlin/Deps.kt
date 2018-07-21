object deps {

    val gradlePluginVersion = "3.3.0-alpha03"
    val kotlinVersion = "1.2.51"
    val supportVersion = "27.1.1"
    val glideVersion = "4.7.1"
    val okhttpVersion = "3.10.0"
    val gsonVersion = "2.8.5"
    val ankoVersion = "0.10.5"
    val bottombarVersion = "2.3.1"
    val constraintVersion = "1.1.2"

    object plugin {
        val gradle = "com.android.tools.build:gradle:$gradlePluginVersion"
        val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    }

    object kotlin {
        val stdlibJdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion"
    }

    object support {
        val compat = "com.android.support:appcompat-v7:$supportVersion"
        val design = "com.android.support:design:$supportVersion"
        val cardview = "com.android.support:cardview-v7:$supportVersion"
        val constraint = "com.android.support.constraint:constraint-layout:$constraintVersion"
    }

    object others {
        val glide = "com.github.bumptech.glide:glide:$glideVersion"
        val okhttp = "com.squareup.okhttp3:okhttp:$okhttpVersion"
        val gson = "com.google.code.gson:gson:$gsonVersion"
        val anko = "org.jetbrains.anko:anko:$ankoVersion"
        val bottombar = "com.roughike:bottom-bar:$bottombarVersion"
    }

    object test {
        val junit = "junit:junit:4.12"
        val runner = "com.android.support.test:runner:1.0.2"
        val espressoCore = "com.android.support.test.espresso:espresso-core:3.0.2"
    }
}