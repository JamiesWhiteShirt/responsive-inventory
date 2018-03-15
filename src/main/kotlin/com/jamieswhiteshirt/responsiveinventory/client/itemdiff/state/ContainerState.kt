package com.jamieswhiteshirt.responsiveinventory.client.itemdiff.state

import net.minecraft.inventory.Container

data class ContainerState(val inventorySlots: List<SlotState>) {
    constructor(container: Container) : this(container.inventorySlots.map(::SlotState))
}
