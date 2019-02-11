plugins {
  id("com.android.application")
  id("kotlin-android")
  id("kotlin-android-extensions")
}

android {
  compileSdkVersion(28)
  defaultConfig {
    applicationId = "cn.com.timeriver.httpsproject"
    minSdkVersion(21)
    targetSdkVersion(28)
    versionCode = 1010
    versionName = "1.0.1"
    testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
    }
  }
}

dependencies {
  implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
  testImplementation(deps.test.junit)
  testImplementation(deps.test.runner)
  testImplementation(deps.test.espressoCore)

  implementation(deps.kotlin.stdlibJdk7)
  implementation(deps.support.compat)
  implementation(deps.support.constraint)
  implementation(deps.others.anko)
  implementation(deps.others.okhttp)
}