package com.jamieswhiteshirt.responsiveinventory.client.itemdiff.state

import net.minecraft.client.entity.EntityPlayerSP

data class PlayerState(
        val inventory: PlayerInventoryState,
        val openContainer: ContainerState?
) {
    constructor(player: EntityPlayerSP) : this(
            PlayerInventoryState(player.inventory),
            player.openContainer?.let(::ContainerState)
    )
}
