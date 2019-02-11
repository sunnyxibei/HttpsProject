object deps {

  const val gradlePluginVersion = "3.2.0"
  const val kotlinVersion = "1.3.11"
  const val supportVersion = "27.1.1"
  const val glideVersion = "4.7.1"
  const val okhttpVersion = "3.10.0"
  const val gsonVersion = "2.8.5"
  const val ankoVersion = "0.10.5"
  const val bottombarVersion = "2.3.1"
  const val constraintVersion = "1.1.2"

  object plugin {
    const val gradle = "com.android.tools.build:gradle:$gradlePluginVersion"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
  }

  object kotlin {
    const val stdlibJdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion"
  }

  object support {
    const val compat = "com.android.support:appcompat-v7:$supportVersion"
    const val design = "com.android.support:design:$supportVersion"
    const val cardview = "com.android.support:cardview-v7:$supportVersion"
    const val constraint = "com.android.support.constraint:constraint-layout:$constraintVersion"
  }

  object others {
    const val glide = "com.github.bumptech.glide:glide:$glideVersion"
    const val okhttp = "com.squareup.okhttp3:okhttp:$okhttpVersion"
    const val gson = "com.google.code.gson:gson:$gsonVersion"
    const val anko = "org.jetbrains.anko:anko:$ankoVersion"
    const val bottombar = "com.roughike:bottom-bar:$bottombarVersion"
  }

  object test {
    const val junit = "junit:junit:4.12"
    const val runner = "com.android.support.test:runner:1.0.2"
    const val espressoCore = "com.android.support.test.espresso:espresso-core:3.0.2"
  }
}