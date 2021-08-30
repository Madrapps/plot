// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val compose_version by extra("1.0.1")
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt").version("1.10.0")
    id("io.github.gradle-nexus.publish-plugin").version("1.1.0")
}

allprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    detekt {
        toolVersion = "1.10.0"
        config = files("${project.rootDir}/config/detekt.yml")
        buildUponDefaultConfig = true

        reports {
            html.enabled = true
            xml.enabled = false
            txt.enabled = false
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

// Publish to Maven Central
nexusPublishing {
    repositories {
        sonatype {
            username.set(System.getenv("OSS_USERNAME"))
            password.set(System.getenv("OSS_PASSWORD"))
            stagingProfileId.set(System.getenv("OSS_STAGING_PROFILE_ID"))
        }
    }
}