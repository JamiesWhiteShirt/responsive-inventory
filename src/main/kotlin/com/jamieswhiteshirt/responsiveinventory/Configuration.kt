package com.jamieswhiteshirt.responsiveinventory

import net.minecraftforge.common.config.Config

@Config(modid = ResponsiveInventory.MODID)
object Configuration {
    @JvmField
    @Config.Comment("It's a test yo")
    var test = 0

    @JvmField
    val testCategory = ItemSounds()

    class ItemSounds {
        @Config.Comment("Play sounds for items in inventories")
        @JvmField
        var inventory = true

        @Config.Comment("Play sounds for items in main hand")
        @JvmField
        var mainHand = true

        @Config.Comment("Play sounds for items in off hand")
        @JvmField
        var offHand = true
    }
}