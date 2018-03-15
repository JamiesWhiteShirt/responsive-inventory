package com.jamieswhiteshirt.responsiveinventory.client.itemdiff.state

import net.minecraft.entity.player.InventoryPlayer

data class PlayerInventoryState(
        val currentItem: ItemStackState,
        val itemStack: ItemStackState,
        val mainInventory: List<ItemStackState>,
        val armorInventory: List<ItemStackState>,
        val offHandInventory: List<ItemStackState>
) {
    constructor(inventory: InventoryPlayer) : this(
            ItemStackState(inventory.mainInventory[inventory.currentItem]),
            ItemStackState(inventory.itemStack),
            inventory.mainInventory.map(::ItemStackState),
            inventory.armorInventory.map(::ItemStackState),
            inventory.offHandInventory.map(::ItemStackState)
    )
}
