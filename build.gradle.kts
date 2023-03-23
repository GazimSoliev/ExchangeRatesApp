import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.gazim.app.exchange_rates"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.material3)
                implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
                implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.14.2")
                implementation("ca.gosyer:compose-material-dialogs-datetime:0.9.2")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ExchangeRatesApp"
            packageVersion = "1.0.0"
        }
    }
}
