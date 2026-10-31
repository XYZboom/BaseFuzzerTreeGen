import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("jvm")
    `java-library`
    id("com.vanniktech.maven.publish") version "0.31.0"
}

kotlin {
    jvmToolchain(11)
    compilerOptions {
        moduleName.set("tree_generator_common")
        freeCompilerArgs.add("-Xjvm-default=all")
    }
    sourceSets.named("main") {
        kotlin.srcDir("../upstream/kotlin/tree-generator-common/src")
        kotlin.srcDir("../upstream/kotlin/generators/main")
        kotlin.srcDir("../upstream/kotlin/compiler-support/src")
    }
}

java { withSourcesJar() }

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
    coordinates(group.toString(), "tree-generator", version.toString())
    pom {
        name = "Kotlin Tree Generator"
        description = "Standalone generic Kotlin tree model and source generator derived from JetBrains Kotlin."
        url = "https://github.com/XYZboom/BaseFuzzerTreeGen"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "repo"
            }
        }
        developers {
            developer {
                id = "XYZboom"
                name = "Xiaotian Ma"
                email = "xyzboom@qq.com"
                url = "https://github.com/XYZboom"
            }
        }
        scm {
            url = "https://github.com/XYZboom/BaseFuzzerTreeGen"
            connection = "scm:git:git://github.com/XYZboom/BaseFuzzerTreeGen.git"
            developerConnection = "scm:git:ssh://git@github.com/XYZboom/BaseFuzzerTreeGen.git"
        }
    }
}

tasks.processResources {
    from("../upstream/kotlin/license/LICENSE.txt") {
        into("META-INF")
        rename { "LICENSE-kotlin.txt" }
    }
    from("../upstream/kotlin/license/NOTICE.txt") {
        into("META-INF")
        rename { "NOTICE-kotlin.txt" }
    }
}

dependencies {
    // Selected Kotlin compiler support sources are vendored at the pinned upstream revision.
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
}

tasks.test { useJUnitPlatform() }
