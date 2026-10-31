plugins {
    kotlin("jvm")
    `java-library`
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
