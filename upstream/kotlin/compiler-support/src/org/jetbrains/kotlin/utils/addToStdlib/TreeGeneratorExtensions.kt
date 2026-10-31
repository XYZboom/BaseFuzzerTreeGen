/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 * Extracted from Kotlin revision 9668d756cb84bc09a64498bcc0c84d75b4456d5a.
 */

@file:JvmName("AddToStdlibKt")

package org.jetbrains.kotlin.utils.addToStdlib

inline fun <T, C : Collection<T>, O> C.ifNotEmpty(body: C.() -> O?): O? =
    if (isNotEmpty()) this.body() else null

inline fun <T, O> Array<out T>.ifNotEmpty(body: Array<out T>.() -> O?): O? =
    if (isNotEmpty()) this.body() else null

fun <T, A : Appendable> Iterable<T>.joinToWithBuffer(
    buffer: A,
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    appendElement: A.(T) -> Unit,
): A {
    buffer.append(prefix)
    var count = 0
    for (element in this) {
        if (++count > 1) buffer.append(separator)
        if (limit < 0 || count <= limit) {
            buffer.appendElement(element)
        } else break
    }
    if (limit in 0..<count) buffer.append(truncated)
    buffer.append(postfix)
    return buffer
}
