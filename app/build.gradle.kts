
import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}


val localPropertiesFile = rootProject.file("local.properties")
val localProperties = Properties()
localProperties.load(FileInputStream(localPropertiesFile))

fun addPropertyIfNotExists(file: File, key:String, value: String) {
    val properties = Properties()
    if(file.exists()) {
        file.inputStream().use { inputStream ->
            properties.load(inputStream)
        }
    }
    if(!properties.containsKey(key)) {

        file.outputStream().use { outputStream ->
            properties.setProperty(key, value)
            properties.store(outputStream, null)
        }
    }
}

addPropertyIfNotExists(localPropertiesFile, "host", "\"localhost\"")
addPropertyIfNotExists(localPropertiesFile, "port", "9091")
addPropertyIfNotExists(localPropertiesFile, "user", "\"grimgdl\"")
addPropertyIfNotExists(localPropertiesFile, "pass", "\"\"")


android {
    compileSdk = 35
    namespace = "com.grimgdl.rtspgrim"

    defaultConfig {
        applicationId = "com.grimgdl.rtspgrim"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String","HOST", localProperties["host"] as String)
        buildConfigField("Integer","PORT", localProperties["port"] as String)
        buildConfigField("String","USER", localProperties["user"] as String)
        buildConfigField("String","PASS", localProperties["pass"] as String)
    }

    buildTypes {
        release {
            isMinifyEnabled =  false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

}

dependencies {
    implementation(platform(libs.compose.bom))


    implementation(libs.compose.ui)
    implementation(libs.compose.preview)
    implementation(libs.compose.material)
    implementation(libs.compose.activity)
    debugImplementation(libs.compose.tooling)

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.libvlc.all)
}