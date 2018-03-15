package com.jamieswhiteshirt.responsiveinventory.client.itemdiff.diff

import com.jamieswhiteshirt.responsiveinventory.client.itemdiff.state.PlayerState

data class PlayerDiff(
    val inventory: PlayerInventoryDiff,
    val openContainer: ContainerDiff?
)

fun diffPlayer(before: PlayerState, after: PlayerState) = PlayerDiff(
    diffPlayerInventory(before.inventory, after.inventory),
    if (before.openContainer != null && after.openContainer != null) {
        diffContainer(before.openContainer, after.openContainer)
    } else null
)
