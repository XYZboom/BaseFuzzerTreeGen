# tree-gen

## Project origin

**tree-gen is developed based on source code from the JetBrains Kotlin repository.** Its current compatible generator implementation vendors Kotlin's Apache-2.0 `generators/tree-generator-common` sources at commit `9668d756cb84bc09a64498bcc0c84d75b4456d5a`, plus three required helper sources. These are upstream-derived sources, not original tree-gen code; tree-gen is not an official JetBrains project. The original package names are retained for compatibility. Provenance, copied-file scope, and license notices are recorded in [`upstream/UPSTREAM.md`](upstream/UPSTREAM.md).

The Gradle module compiles these sources against the Kotlin compiler binary artifact; it does not require a Kotlin compiler source checkout or the existing `tree-generator-common.jar`.

## Module organization

Keep the project focused on **generic tree-model and source-generation machinery only**. The main library module should contain the generic model/configuration, type/import handling, and printers. It must not contain a concrete IR model, a `TreeBuilder` for CrossLangFuzzer, IR-specific printers, or generated IR sources.

The vendored source groups are already naturally separated by package:

- `org.jetbrains.kotlin.generators.tree`: generic element/field/type model and configuration (23 files)
- `org.jetbrains.kotlin.generators.tree.config`: generic configuration helpers (3 files)
- `org.jetbrains.kotlin.generators.tree.imports`: type rendering and import collection (4 files)
- `org.jetbrains.kotlin.generators.tree.printer`: generic source printers and `TreeGenerator` (2 files)
- `org.jetbrains.kotlin.generators.util`: three vendored support helpers needed to build/run the generic module

The project has one `tree-generator` library module. Its main source roots contain the vendored generic Kotlin sources and required helpers only. No project-specific tree model or generator is included. See [the source guide](docs/kotlin-tree-generator.md) for the model layers, generation flow, helper roles, and integration boundary.

## Compatibility objective

The first milestone is to keep CrossLangFuzzer's current upstream-compatible generator API and behavior available from this standalone artifact. The exact 32 source files embedded in CrossLangFuzzer's existing JAR were confirmed byte-for-byte against the vendored sources, and all 91 public class signatures match the old JAR. The current CrossLangFuzzer generator sources compiled and ran against this copy in an isolated scratch directory. It produced the same 61 generated file paths; all file contents match after removing the vendored Kotlin copyright header. The original `tree/gen` directory was not modified.

The full CrossLangFuzzer generator wiring (its `TreeBuilder`, IR-specific printers, and entry point) has not been migrated into this repository; it has only been compiled and run externally as a compatibility check.

## Licensing and publication

Kotlin's copied sources are under Apache License 2.0. The source, `LICENSE`, `NOTICE`, and `COPYRIGHT` files are retained under `upstream/kotlin/`. Preserve those notices and mark changes to copied files. Audit the compiler binary dependency's license and publication metadata before release. Maven Central publication is not configured yet.

Before publishing, verify the final source/artifact contents, dependency licenses, required notices, and Maven Central signing/metadata requirements.

## Build

Use the included Gradle Wrapper and JDK 11 or newer:

```sh
./gradlew :tree-generator:test :tree-generator:jar :tree-generator:sourcesJar
```

The resulting binary, source, and retained license/NOTICE artifacts are written under `tree-generator/build/libs/`. Maven Central publication and permanent CrossLangFuzzer Gradle wiring are still pending.
