# Kotlin tree-generator-common 源码说明

## 项目定位与来源

本项目当前提供 Kotlin 编译器仓库 `generators/tree-generator-common` 的通用树模型和源码生成能力，不包含任何具体语言/IR 的树定义、`TreeBuilder`、领域专用 printer 或生成后的树源码。

当前上游快照为 Kotlin commit [`9668d756cb84bc09a64498bcc0c84d75b4456d5a`](https://github.com/JetBrains/kotlin/commit/9668d756cb84bc09a64498bcc0c84d75b4456d5a)。`upstream/kotlin/tree-generator-common/src` 中的 32 个 Kotlin 文件、`upstream/kotlin/generators/main` 中的 3 个辅助文件，以及 `upstream/kotlin/compiler-support/src` 中选出的 compiler 支持源码均来自该上游版本。`addToStdlib.kt` 只摘取 tree-gen 实际使用的两个 `ifNotEmpty` 重载和 `joinToWithBuffer`；`GeneratorsFileUtil.kt` 有标注的 JDK-only 本地适配。文件清单、修改记录、许可证和 NOTICE 见 [`../upstream/UPSTREAM.md`](../upstream/UPSTREAM.md)。这些均为 JetBrains Kotlin 上游衍生源码；本项目不是 JetBrains 官方项目。

## 源码分层

Gradle 子模块 `tree-generator` 将以下上游目录作为 Kotlin main source roots 编译；源码按上游目录保留，便于追溯来源。

| 上游包/源码目录 | 主要职责 |
| --- | --- |
| `org.jetbrains.kotlin.generators.tree` | 通用树模型：`AbstractElement`、字段、实现类、类型引用、继承关系与 visitor/transformer 描述 |
| `org.jetbrains.kotlin.generators.tree.config` | 构建元素模型的配置基础设施和抽象 builder/configurator |
| `org.jetbrains.kotlin.generators.tree.imports` | 可导入类型、导入收集、类型引用的渲染 |
| `org.jetbrains.kotlin.generators.tree.printer` | 通用生成文件管理、元素/实现/Builder/visitor printer 的抽象及输出流程 |
| `org.jetbrains.kotlin.generators.util` | 上游提供的文件生成工具、接口/抽象类求解及 printer 辅助实现；源码位于 `upstream/kotlin/generators/main` |
| `org.jetbrains.kotlin.descriptors` / `org.jetbrains.kotlin.types` / `org.jetbrains.kotlin.utils` / `org.jetbrains.kotlin.util` | 为保持上游 API 所需的少量类型和工具源码；具体文件见 provenance 清单 |

本项目已用所选上游源码提供 `Modality`、`Variance`、`IndentingPrinter`、`SmartPrinter` 等类型/工具，不再依赖 `kotlin-compiler` 二进制。运行时仍依赖 `kotlin-reflect`、Kotlin stdlib 和传递引入的 `org.jetbrains:annotations`；Kotlin Gradle plugin 仅为构建依赖。因此当前并非零运行时依赖。复制的支持类型保留 `org.jetbrains.kotlin.*` 原包名；若消费方同时引入提供同名类型的 compiler artifact，可能产生类路径重复，因此不应混用。`moduleName=tree_generator_common` 与 `-Xjvm-default=all` 用于匹配原模块的 JVM API 形态。

## 通用生成流程

1. **定义模型**：消费者根据自己的树结构实现抽象 element、field、implementation 等类型，并通过上游的配置 API 构建 `Model`。这些领域类型和配置代码属于消费者，不属于 tree-gen。
2. **解析模型**：`Model` 支持字段继承和 visitor/transform 子节点能力传播；`InterfaceAndAbstractClassConfigurator` 根据父子图推导接口、抽象类及 sealed 类型。
3. **准备类型和导入**：`TypeRef`、`ClassRef` 及 `imports` 包负责类型参数化、替换、渲染和 import 收集。
4. **提供打印器**：消费者实现适合自身领域的 element、field、implementation、builder、visitor printer；`tree/printer` 提供可复用的打印基础。
5. **输出文件**：`TreeGenerator` 收集待生成内容并写入目标目录。上游 `GeneratorsFileUtil` 会避免重写内容未变的文件，并清理目标目录中带有生成标记、但本轮不再生成的旧文件。运行时应将输出目录限定在生成文件专用目录。

因此，tree-gen 是**生成框架/抽象能力**，不是开箱即用的某个具体 IR 生成器；接入方还需提供其模型、配置和专用 printer。

## 构建与验证

```sh
./gradlew :tree-generator:test :tree-generator:jar :tree-generator:sourcesJar
```

测试只针对通用 API 的编译和基本调用，不包含 CrossLangFuzzer 的具体模型。CrossLangFuzzer 的生成器曾在独立 scratch 目录中作为兼容性检查运行，但其源码并未复制进本项目。

## 许可与修改

上游源码遵循 Apache License 2.0。分发或修改时保留相应版权/许可声明和 NOTICE，并在改动的上游文件中标注本地修改。具体文件清单与来源记录以 [`../upstream/UPSTREAM.md`](../upstream/UPSTREAM.md) 为准。
