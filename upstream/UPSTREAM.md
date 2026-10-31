# Vendored source provenance

This directory contains an unmodified source copy of JetBrains Kotlin's `generators/tree-generator-common` module at commit `9668d756cb84bc09a64498bcc0c84d75b4456d5a`. The 32 Kotlin files were byte-for-byte compared with the Kotlin sources embedded in the CrossLangFuzzer `tree-generator-common.jar`; all matched. The copy keeps the upstream `org.jetbrains.kotlin.generators.*` package names. Within the shared `org.jetbrains.kotlin.generators.*` class set, 93 of 95 classes have identical public signatures; the differences are an additional default `ElementOrRef.copy(boolean)` method and the constructor descriptor of package-private `NodeImpl`. The new JAR intentionally does not bundle the old JAR's compiler/runtime class set, so this is not whole-JAR binary compatibility. CrossLangFuzzer's generator compiles and runs against the compiler-artifact-free tree-gen, producing 61/61 matching generated file bodies after normalizing the added Kotlin copyright header.

The standalone build also vendors three required helper sources from `generators/main` and a minimal subset of compiler support sources from the same Kotlin commit:

- `kotlin/generators/main/GeneratorsFileUtil.kt`
- `kotlin/generators/main/InterfaceAbstractClassSolver.kt`
- `kotlin/generators/main/PrinterUtils.kt`

Their imports are required by `tree-generator-common`; their original package names and copyright/license headers are preserved.

The compiler-support source subset consists of eight unmodified upstream files:

- `core/compiler.common/src/org/jetbrains/kotlin/descriptors/Modality.kt`
- `core/compiler.common/src/org/jetbrains/kotlin/types/Variance.kt`
- `core/compiler.common/src/org/jetbrains/kotlin/util/containerUtils.kt`
- `core/util.runtime/src/org/jetbrains/kotlin/utils/IndentingPrinter.kt`
- `core/util.runtime/src/org/jetbrains/kotlin/utils/Printer.kt`
- `core/util.runtime/src/org/jetbrains/kotlin/utils/SmartPrinter.kt`
- `core/util.runtime/src/org/jetbrains/kotlin/utils/capitalizeDecapitalize.kt`
- `compiler/util/src/org/jetbrains/kotlin/utils/DummyDelegate.kt`

`org.jetbrains.kotlin.utils.addToStdlib/TreeGeneratorExtensions.kt` contains only the two `ifNotEmpty` overloads and `joinToWithBuffer` extracted from upstream `core/util.runtime/.../addToStdlib.kt`; `@file:JvmName("AddToStdlibKt")` preserves the upstream JVM facade name for these functions. The full utility file is intentionally not copied because it contains unrelated IntelliJ/compiler support. `GeneratorsFileUtil.kt` is locally adapted to replace IntelliJ `SystemInfo`/`StringUtil` with JDK equivalents and to create temporary output beside the destination. The extracted functions retain their upstream copyright notice; all copied and adapted files remain Apache-2.0-derived.

- Upstream module: https://github.com/JetBrains/kotlin/tree/9668d756cb84bc09a64498bcc0c84d75b4456d5a/generators/tree-generator-common
- Upstream helpers: https://github.com/JetBrains/kotlin/tree/9668d756cb84bc09a64498bcc0c84d75b4456d5a/generators/main
- Upstream commit: https://github.com/JetBrains/kotlin/commit/9668d756cb84bc09a64498bcc0c84d75b4456d5a
- License: Apache License 2.0; see `kotlin/license/LICENSE.txt`.
- Copyright notice: `kotlin/license/COPYRIGHT.txt`.
- NOTICE: `kotlin/license/NOTICE.txt`.

The generic module and seven complete compiler-support source files are unmodified upstream copies. `capitalizeDecapitalize.kt` differs only by trimming excess blank lines at end of file; the extension subset and `GeneratorsFileUtil.kt` adaptation are explicitly marked above. Retain upstream notices and mark any future changes in affected files. This is upstream-derived code, not independently authored code. The compiler support files retain `org.jetbrains.kotlin.*` package names; consumers should not also put a Kotlin compiler artifact containing the same classes on the same classpath.

## Standalone build status

`tree-generator/build.gradle.kts` no longer depends on the `kotlin-compiler` artifact: it compiles against the selected support sources above. Runtime dependencies include `kotlin-reflect`, Kotlin stdlib, and transitive `org.jetbrains:annotations`; the Kotlin Gradle plugin is build-time tooling, not a published runtime dependency. Therefore this removes the compiler artifact, but does not make the library literally dependency-free. The upstream module's original build file refers to Kotlin monorepo projects; those project references are not used in this standalone build. The standalone project contains only generic generator sources and required support helpers; it does not include a project-authored generator prototype or CrossLangFuzzer-specific tree model/printers. See `docs/kotlin-tree-generator.md` for details.
