package com.jamieswhiteshirt.responsiveinventory.client

import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraft.util.registry.RegistrySimple

class ItemSoundRegistry : RegistrySimple<ResourceLocation, SoundEvent>() {
    private lateinit var itemSoundRegistry: MutableMap<ResourceLocation, SoundEvent>

    override fun createUnderlyingMap(): MutableMap<ResourceLocation, SoundEvent> {
        itemSoundRegistry = hashMapOf()
        return itemSoundRegistry
    }

    fun clearMap() = itemSoundRegistry.clear()
}