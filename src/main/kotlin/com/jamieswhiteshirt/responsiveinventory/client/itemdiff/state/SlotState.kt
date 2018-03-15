package com.jamieswhiteshirt.responsiveinventory.client.itemdiff.state

import net.minecraft.inventory.Slot

data class SlotState(
    val xPos: Int,
    val yPos: Int,
    val stack: ItemStackState
) {
    constructor(slot: Slot): this(slot.xPos, slot.yPos, ItemStackState(slot.stack))
}