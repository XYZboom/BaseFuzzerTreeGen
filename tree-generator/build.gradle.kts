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
    // Pinned compiler binaries provide Kotlin's types/utilities; no compiler source checkout is needed.
    api("org.jetbrains.kotlin:kotlin-compiler:2.1.21")
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
}

tasks.test { useJUnitPlatform() }
