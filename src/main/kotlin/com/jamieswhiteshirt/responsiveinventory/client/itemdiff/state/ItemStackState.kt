package com.jamieswhiteshirt.responsiveinventory.client.itemdiff.state

import com.jamieswhiteshirt.responsiveinventory.client.itemdiff.ItemDiscriminator
import net.minecraft.item.ItemStack

data class ItemStackState(val discriminator: ItemDiscriminator, val count: Int) {
    constructor(stack: ItemStack) : this(ItemDiscriminator(stack.item, stack.itemDamage), stack.count)
}
