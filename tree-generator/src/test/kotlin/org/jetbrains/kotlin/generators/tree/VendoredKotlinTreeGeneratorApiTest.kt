package org.jetbrains.kotlin.generators.tree

import kotlin.test.Test
import kotlin.test.assertEquals
import org.jetbrains.kotlin.generators.tree.StandardTypes
import org.jetbrains.kotlin.generators.tree.imports.ImportCollecting

class VendoredKotlinTreeGeneratorApiTest {
    @Test
    fun `vendored standard type renders through the preserved api`() {
        val output = StringBuilder()
        StandardTypes.string.renderTo(output, ImportCollecting.Empty)
        assertEquals("String", output.toString())
    }
}
