package com.jamieswhiteshirt.responsiveinventory.client.itemdiff.diff

import com.jamieswhiteshirt.responsiveinventory.client.itemdiff.state.ContainerState
import com.jamieswhiteshirt.responsiveinventory.client.itemdiff.state.SlotState

data class ContainerDiff(
    val inventorySlots: List<EditScriptItem<SlotState>>
)

fun diffContainer(before: ContainerState, after: ContainerState) = ContainerDiff(
    editScript(before.inventorySlots, after.inventorySlots)
)
