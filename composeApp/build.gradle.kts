import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.util.UUID

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.libres)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.sqlDelight)
    alias(libs.plugins.conveyor)

}

version = "1.0"

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(libs.libres)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.koin)
            implementation(libs.composeImageLoader)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.composeIcons.featherIcons)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.multiplatformSettings)
            implementation(libs.koin.core)
            implementation(libs.kotlin.multiplatform.appdirs)
            implementation(libs.richeditor.compose)
            implementation(libs.sqlDelight.coroutines)
            implementation("com.konyaco:fluent:0.0.1-dev.6")
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        jvmMain.dependencies {
            implementation(compose.desktop.common)
            implementation(compose.desktop.currentOs)
            implementation(libs.sqlDelight.driver.sqlite)
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Exe, TargetFormat.Deb)
            includeAllModules = true
            packageName = "TrayNote"
            version = "1.0.3"

            windows {
                packageVersion = "TrayNote"
                exePackageVersion = "1.0.3"
                upgradeUuid = "1502c32b-a260-4100-9ee8-9dc2c50ef7b8"
                iconFile.set(project.file("/resources/icon.ico"))
            }
        }
    }
}

libres {
    // https://github.com/Skeptick/libres#setup
}
tasks.getByPath("jvmProcessResources").dependsOn("libresGenerateResources")
tasks.getByPath("jvmSourcesJar").dependsOn("libresGenerateResources")

buildConfig {
    // BuildConfig configuration here.
    // https://github.com/gmazzo/gradle-buildconfig-plugin#usage-in-kts
}

sqldelight {
    databases {
        create("Database") {
            // Database configuration here.
            // https://cashapp.github.io/sqldelight
            packageName.set("com.dvinov.traynote.db")
        }
    }
}

