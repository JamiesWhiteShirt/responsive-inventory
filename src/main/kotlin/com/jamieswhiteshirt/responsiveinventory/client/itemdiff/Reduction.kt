package com.jamieswhiteshirt.responsiveinventory.client.itemdiff

import com.jamieswhiteshirt.responsiveinventory.client.itemdiff.diff.*
import com.jamieswhiteshirt.responsiveinventory.client.itemdiff.state.*

typealias Reduction = Set<ItemDiscriminator>

fun reducePlayerDiff(diff: PlayerDiff): Reduction {
    return reduceDiffs(listOf(
        reducePlayerInventoryDiff(diff.inventory),
        (if (diff.openContainer != null) {
            reduceContainerDiff(diff.openContainer)
        } else emptySet())
    ))
}

fun reducePlayerInventoryDiff(diff: PlayerInventoryDiff): Reduction {
    return reduceDiffs(listOf(
        reduceDiff(diff.currentItem),
        reduceDiff(diff.itemStack),
        reduceEditScript(diff.mainInventory, { it }),
        reduceEditScript(diff.armorInventory, { it }),
        reduceEditScript(diff.offhandInventory, { it })
    ))
}

fun reduceContainerDiff(diff: ContainerDiff): Reduction {
    return reduceEditScript(diff.inventorySlots, SlotState::stack)
}

fun <T> reduceEditScript(editScript: List<EditScriptItem<T>>, key: (T) -> ItemStackState): Reduction {
    return editScript.filter {
        it.operation != EditScriptOperation.KEEP
    }.map {
        key(it.item).discriminator
    }.toSet()
}

fun reduceDiff(diff: Diff<ItemStackState>): Reduction = if (diff.before != diff.after) {
    setOf(diff.before.discriminator, diff.after.discriminator)
} else emptySet()

fun reduceDiffs(sets: List<Reduction>): Reduction {
    val result = hashSetOf<ItemDiscriminator>()
    sets.forEach { result.addAll(it) }
    return result
}
