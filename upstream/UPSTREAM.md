# Vendored source provenance

This directory contains an unmodified source copy of JetBrains Kotlin's `generators/tree-generator-common` module at commit `9668d756cb84bc09a64498bcc0c84d75b4456d5a`. The 32 Kotlin files were byte-for-byte compared with the Kotlin sources embedded in the CrossLangFuzzer `tree-generator-common.jar`; all matched. The copy keeps the upstream `org.jetbrains.kotlin.generators.*` package names so current generator code can continue using the same API. After compiling with Kotlin 2.1.21, module name `tree_generator_common`, and `-Xjvm-default=all`, all 91 public class files under the tree-generator package match the old JAR's `javap` signatures exactly.

The standalone build also vendors three required helper sources from the same Kotlin commit:

- `kotlin/generators/main/GeneratorsFileUtil.kt`
- `kotlin/generators/main/InterfaceAbstractClassSolver.kt`
- `kotlin/generators/main/PrinterUtils.kt`

Their imports are required by `tree-generator-common`; their original package names and copyright/license headers are preserved.

- Upstream module: https://github.com/JetBrains/kotlin/tree/9668d756cb84bc09a64498bcc0c84d75b4456d5a/generators/tree-generator-common
- Upstream helpers: https://github.com/JetBrains/kotlin/tree/9668d756cb84bc09a64498bcc0c84d75b4456d5a/generators/main
- Upstream commit: https://github.com/JetBrains/kotlin/commit/9668d756cb84bc09a64498bcc0c84d75b4456d5a
- License: Apache License 2.0; see `kotlin/license/LICENSE.txt`.
- Copyright notice: `kotlin/license/COPYRIGHT.txt`.
- NOTICE: `kotlin/license/NOTICE.txt`.

The vendored source files are currently unmodified. Mark any future changes to copied files in the affected files and retain upstream notices. This is upstream code, not independently authored code. The project plans to maintain the API going forward; package relocation, public API changes, and publication must preserve attribution and comply with the applicable license.

## Standalone build status

`tree-generator/build.gradle.kts` compiles the vendored sources without a Kotlin compiler source checkout. It currently uses the binary dependency `org.jetbrains.kotlin:kotlin-compiler:2.1.21` to supply compiler utility/type classes, plus Kotlin reflection. The upstream module's original build file refers to Kotlin monorepo projects; those project references are not used in this standalone build. The standalone project contains only the vendored generic generator sources and the required support helpers; it does not include a project-authored generator prototype or any CrossLangFuzzer-specific tree model/printers. See `docs/kotlin-tree-generator.md` for the source architecture and generation flow.
