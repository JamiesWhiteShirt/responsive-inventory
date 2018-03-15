package com.jamieswhiteshirt.responsiveinventory.client.itemdiff.diff

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object EditScriptSpec : Spek({
    describe("editScript") {
        it("should remove then add for completely different equally sized lists") {
            val a = listOf(0, 1, 2)
            val b = listOf(3, 4, 5)

            assertEquals(listOf(
                EditScriptItem(0, EditScriptOperation.REMOVE),
                EditScriptItem(1, EditScriptOperation.REMOVE),
                EditScriptItem(2, EditScriptOperation.REMOVE),
                EditScriptItem(3, EditScriptOperation.ADD),
                EditScriptItem(4, EditScriptOperation.ADD),
                EditScriptItem(5, EditScriptOperation.ADD)
            ), editScript(a, b))
            assertEquals(listOf(
                EditScriptItem(3, EditScriptOperation.REMOVE),
                EditScriptItem(4, EditScriptOperation.REMOVE),
                EditScriptItem(5, EditScriptOperation.REMOVE),
                EditScriptItem(0, EditScriptOperation.ADD),
                EditScriptItem(1, EditScriptOperation.ADD),
                EditScriptItem(2, EditScriptOperation.ADD)
            ), editScript(b, a))
        }

        it("should keep equal lists") {
            val a = listOf(0, 1, 2)
            val b = listOf(0, 1, 2)

            assertEquals(listOf(
                EditScriptItem(0, EditScriptOperation.KEEP),
                EditScriptItem(1, EditScriptOperation.KEEP),
                EditScriptItem(2, EditScriptOperation.KEEP)
            ), editScript(a, b))
        }

        it("should keep the common subsequence for equally sized lists") {
            val a = listOf('a', 'b', 'c')
            val b = listOf('b', 'c', 'd')

            assertEquals(listOf(
                EditScriptItem('a', EditScriptOperation.REMOVE),
                EditScriptItem('b', EditScriptOperation.KEEP),
                EditScriptItem('c', EditScriptOperation.KEEP),
                EditScriptItem('d', EditScriptOperation.ADD)
            ), editScript(a, b))
            assertEquals(listOf(
                EditScriptItem('a', EditScriptOperation.ADD),
                EditScriptItem('b', EditScriptOperation.KEEP),
                EditScriptItem('c', EditScriptOperation.KEEP),
                EditScriptItem('d', EditScriptOperation.REMOVE)
            ), editScript(b, a))
        }

        it("should keep the common item for equally sized lists") {
            val a = listOf('a', 'b', 'c')
            val b = listOf('c', 'd', 'e')

            assertEquals(listOf(
                EditScriptItem('a', EditScriptOperation.REMOVE),
                EditScriptItem('b', EditScriptOperation.REMOVE),
                EditScriptItem('c', EditScriptOperation.KEEP),
                EditScriptItem('d', EditScriptOperation.ADD),
                EditScriptItem('e', EditScriptOperation.ADD)
            ), editScript(a, b))
            assertEquals(listOf(
                EditScriptItem('a', EditScriptOperation.ADD),
                EditScriptItem('b', EditScriptOperation.ADD),
                EditScriptItem('c', EditScriptOperation.KEEP),
                EditScriptItem('d', EditScriptOperation.REMOVE),
                EditScriptItem('e', EditScriptOperation.REMOVE)
            ), editScript(b, a))
        }

        it("should remove then add for completely different differently sized lists") {
            val a = listOf(0, 1)
            val b = listOf(2, 3, 4)

            assertEquals(listOf(
                EditScriptItem(0, EditScriptOperation.REMOVE),
                EditScriptItem(1, EditScriptOperation.REMOVE),
                EditScriptItem(2, EditScriptOperation.ADD),
                EditScriptItem(3, EditScriptOperation.ADD),
                EditScriptItem(4, EditScriptOperation.ADD)
            ), editScript(a, b))
            assertEquals(listOf(
                EditScriptItem(2, EditScriptOperation.REMOVE),
                EditScriptItem(3, EditScriptOperation.REMOVE),
                EditScriptItem(4, EditScriptOperation.REMOVE),
                EditScriptItem(0, EditScriptOperation.ADD),
                EditScriptItem(1, EditScriptOperation.ADD)
            ), editScript(b, a))
        }

        it("should keep the common subsequence for differently sized lists") {
            val a = listOf('a', 'b', 'c')
            val b = listOf('b', 'c')

            assertEquals(listOf(
                EditScriptItem('a', EditScriptOperation.REMOVE),
                EditScriptItem('b', EditScriptOperation.KEEP),
                EditScriptItem('c', EditScriptOperation.KEEP)
            ), editScript(a, b))
            assertEquals(listOf(
                EditScriptItem('a', EditScriptOperation.ADD),
                EditScriptItem('b', EditScriptOperation.KEEP),
                EditScriptItem('c', EditScriptOperation.KEEP)
            ), editScript(b, a))
        }

        it("should keep the common item for differently sized lists") {
            val a = listOf('a', 'b', 'c')
            val b = listOf('c', 'd')

            assertEquals(listOf(
                EditScriptItem('a', EditScriptOperation.REMOVE),
                EditScriptItem('b', EditScriptOperation.REMOVE),
                EditScriptItem('c', EditScriptOperation.KEEP),
                EditScriptItem('d', EditScriptOperation.ADD)
            ), editScript(a, b))
            assertEquals(listOf(
                EditScriptItem('a', EditScriptOperation.ADD),
                EditScriptItem('b', EditScriptOperation.ADD),
                EditScriptItem('c', EditScriptOperation.KEEP),
                EditScriptItem('d', EditScriptOperation.REMOVE)
            ), editScript(b, a))
        }

        it("should handle repeated items") {
            val a = listOf('a', 'a', 'a', 'a')
            val b = listOf('a', 'a', 'b', 'a')

            assertEquals(listOf(
                EditScriptItem('a', EditScriptOperation.KEEP),
                EditScriptItem('a', EditScriptOperation.KEEP),
                EditScriptItem('b', EditScriptOperation.ADD),
                EditScriptItem('a', EditScriptOperation.KEEP),
                EditScriptItem('a', EditScriptOperation.REMOVE)
            ), editScript(a, b))
            assertEquals(listOf(
                EditScriptItem('a', EditScriptOperation.KEEP),
                EditScriptItem('a', EditScriptOperation.KEEP),
                EditScriptItem('b', EditScriptOperation.REMOVE),
                EditScriptItem('a', EditScriptOperation.KEEP),
                EditScriptItem('a', EditScriptOperation.ADD)
            ), editScript(b, a))
        }
    }
})