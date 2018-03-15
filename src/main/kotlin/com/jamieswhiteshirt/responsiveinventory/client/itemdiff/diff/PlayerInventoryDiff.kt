package com.jamieswhiteshirt.responsiveinventory.client.itemdiff.diff

import com.jamieswhiteshirt.responsiveinventory.client.itemdiff.state.ItemStackState
import com.jamieswhiteshirt.responsiveinventory.client.itemdiff.state.PlayerInventoryState

data class PlayerInventoryDiff(
    val currentItem: Diff<ItemStackState>,
    val itemStack: Diff<ItemStackState>,
    val mainInventory: List<EditScriptItem<ItemStackState>>,
    val armorInventory: List<EditScriptItem<ItemStackState>>,
    val offhandInventory: List<EditScriptItem<ItemStackState>>
)

fun diffPlayerInventory(before: PlayerInventoryState, after: PlayerInventoryState) = PlayerInventoryDiff(
    Diff(before.currentItem, after.currentItem),
    Diff(before.itemStack, after.itemStack),
    editScript(before.mainInventory, after.mainInventory),
    editScript(before.armorInventory, after.armorInventory),
    editScript(before.offHandInventory, after.offHandInventory)
)
