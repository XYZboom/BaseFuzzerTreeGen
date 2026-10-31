# tree-gen

## Project origin

**tree-gen is developed based on source code from the JetBrains Kotlin repository.** It vendors Kotlin's Apache-2.0 `generators/tree-generator-common` sources at commit `9668d756cb84bc09a64498bcc0c84d75b4456d5a`, three generator helpers, and a minimal set of compiler support sources from the same commit. These are upstream-derived sources, not original tree-gen code; tree-gen is not an official JetBrains project. Original package names are retained for compatibility. Provenance, copied-file scope, and license notices are recorded in [`upstream/UPSTREAM.md`](upstream/UPSTREAM.md).

The Gradle module no longer depends on the `kotlin-compiler` binary artifact. Runtime dependencies still include `kotlin-reflect`, Kotlin stdlib, and transitive `org.jetbrains:annotations`; the Kotlin Gradle plugin is build-time tooling. This is not a literal zero-runtime-dependency library.

## Module organization

Keep the project focused on **generic tree-model and source-generation machinery only**. The main library module should contain the generic model/configuration, type/import handling, and printers. It must not contain a concrete IR model, a `TreeBuilder` for CrossLangFuzzer, IR-specific printers, or generated IR sources.

The vendored source groups are already naturally separated by package:

- `org.jetbrains.kotlin.generators.tree`: generic element/field/type model and configuration (23 files)
- `org.jetbrains.kotlin.generators.tree.config`: generic configuration helpers (3 files)
- `org.jetbrains.kotlin.generators.tree.imports`: type rendering and import collection (4 files)
- `org.jetbrains.kotlin.generators.tree.printer`: generic source printers and `TreeGenerator` (2 files)
- `org.jetbrains.kotlin.generators.util`: three vendored support helpers needed to build/run the generic module
- `org.jetbrains.kotlin.descriptors`, `org.jetbrains.kotlin.types`, `org.jetbrains.kotlin.utils`, and `org.jetbrains.kotlin.util`: selected upstream compiler support types/utilities needed by the generic module

The project has one `tree-generator` library module. Its main source roots contain the vendored generic Kotlin sources, three generator helpers, and the minimal compiler-support source subset. No project-specific tree model or generator is included. See [the source guide](docs/kotlin-tree-generator.md) for the model layers, generation flow, helper roles, and integration boundary.

## Compatibility objective

The first milestone is to keep CrossLangFuzzer's generator API and behavior available from this standalone artifact. The exact 32 tree-generator source files embedded in CrossLangFuzzer's old JAR were confirmed byte-for-byte against the vendored sources. In the shared `org.jetbrains.kotlin.generators.*` class set, 93 of 95 classes have identical public signatures; the two differences are an additional default `ElementOrRef.copy(boolean)` method and the constructor descriptor of package-private `NodeImpl`. The new JAR intentionally does not bundle the old JAR's compiler/runtime class set, so this is not whole-JAR binary compatibility. CrossLangFuzzer's generator sources compile and run against the compiler-artifact-free tree-gen in an isolated scratch directory. They produce the same 61 generated file paths and identical contents after normalizing the added copyright header. The original `tree/gen` directory was not modified.

The full CrossLangFuzzer generator wiring (its `TreeBuilder`, IR-specific printers, and entry point) has not been migrated into this repository; it has only been compiled and run externally as a compatibility check.

## Licensing and publication

Kotlin's copied sources are under Apache License 2.0. The source, `LICENSE`, `NOTICE`, and `COPYRIGHT` files are retained under `upstream/kotlin/`. Preserve those notices and mark changes to copied files. `kotlin-compiler` is no longer a runtime dependency; `kotlin-reflect`, stdlib, and transitive annotations remain. The copied support classes keep `org.jetbrains.kotlin.*` names, so consumers should avoid also adding a compiler artifact that supplies those same classes.

Release `io.github.xyzboom:tree-generator:0.1.0` is published to Maven Central. Add it with:

```kotlin
implementation("io.github.xyzboom:tree-generator:0.1.0")
```

## Build

Use the included Gradle Wrapper and JDK 11 or newer:

```sh
./gradlew :tree-generator:test :tree-generator:jar :tree-generator:sourcesJar
```

The resulting binary, source, and retained license/NOTICE artifacts are written under `tree-generator/build/libs/`. Release `io.github.xyzboom:tree-generator:0.1.0` is available from Maven Central.
