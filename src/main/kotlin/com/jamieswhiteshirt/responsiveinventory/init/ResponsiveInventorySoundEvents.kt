package com.jamieswhiteshirt.responsiveinventory.init

import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object ResponsiveInventorySoundEvents {
    val MATERIAL_SOLID_HANDLE = SoundEvent(ResourceLocation("responsiveinventory", "material.solid.handle"))
    val MATERIAL_METALLIC_HANDLE = SoundEvent(ResourceLocation("responsiveinventory", "material.metallic.handle"))
    val MATERIAL_COARSE_HANDLE = SoundEvent(ResourceLocation("responsiveinventory", "material.coarse.handle"))
    val MATERIAL_WOODEN_HANDLE = SoundEvent(ResourceLocation("responsiveinventory", "material.wooden.handle"))
    val MATERIAL_PLANT_HANDLE = SoundEvent(ResourceLocation("responsiveinventory", "material.plant.handle"))
    val MATERIAL_FINE_HANDLE = SoundEvent(ResourceLocation("responsiveinventory", "material.fine.handle"))
    val MATERIAL_FRAGILE_HANDLE = SoundEvent(ResourceLocation("responsiveinventory", "material.fragile.handle"))
    val MATERIAL_SOFT_HANDLE = SoundEvent(ResourceLocation("responsiveinventory", "material.soft.handle"))
    val MATERIAL_SNOWY_HANDLE = SoundEvent(ResourceLocation("responsiveinventory", "material.snowy.handle"))
    val MATERIAL_HEAVY_METALLIC_HANDLE = SoundEvent(ResourceLocation("responsiveinventory", "material.heavy_metallic.handle"))
    val MATERIAL_STICKY_HANDLE = SoundEvent(ResourceLocation("responsiveinventory", "material.sticky.handle"))
    val MATERIAL_WOOD_EQUIPMENT_HANDLE = SoundEvent(ResourceLocation("responsiveinventory", "material.wood_equipment.handle"))
    val MATERIAL_STONE_EQUIPMENT_HANDLE = SoundEvent(ResourceLocation("responsiveinventory", "material.stone_equipment.handle"))
    val MATERIAL_IRON_EQUIPMENT_HANDLE = SoundEvent(ResourceLocation("responsiveinventory", "material.iron_equipment.handle"))
    val MATERIAL_DIAMOND_EQUIPMENT_HANDLE = SoundEvent(ResourceLocation("responsiveinventory", "material.diamond_equipment.handle"))
    val MATERIAL_GOLD_EQUIPMENT_HANDLE = SoundEvent(ResourceLocation("responsiveinventory", "material.gold_equipment.handle"))
    val MATERIAL_LEATHER_EQUIPMENT_HANDLE = SoundEvent(ResourceLocation("responsiveinventory", "material.leather_equipment.handle"))
    val MATERIAL_CHAIN_EQUIPMENT_HANDLE = SoundEvent(ResourceLocation("responsiveinventory", "material.chain_equipment.handle"))
    val MATERIAL_EQUIPMENT_HANDLE = SoundEvent(ResourceLocation("responsiveinventory", "material.equipment.handle"))
    val MATERIAL_GENERIC_HANDLE = SoundEvent(ResourceLocation("responsiveinventory", "material.generic.handle"))
    val MATERIAL_LIGHT_HANDLE = SoundEvent(ResourceLocation("responsiveinventory", "material.light.handle"))
    val MATERIAL_LIQUID_HANDLE = SoundEvent(ResourceLocation("responsiveinventory", "material.liquid.handle"))

    @SubscribeEvent
    fun register(event: RegistryEvent.Register<SoundEvent>) {
        event.registry.apply {
            arrayOf(
                    MATERIAL_SOLID_HANDLE,
                    MATERIAL_METALLIC_HANDLE,
                    MATERIAL_COARSE_HANDLE,
                    MATERIAL_WOODEN_HANDLE,
                    MATERIAL_PLANT_HANDLE,
                    MATERIAL_FINE_HANDLE,
                    MATERIAL_FRAGILE_HANDLE,
                    MATERIAL_SOFT_HANDLE,
                    MATERIAL_SNOWY_HANDLE,
                    MATERIAL_HEAVY_METALLIC_HANDLE,
                    MATERIAL_STICKY_HANDLE,
                    MATERIAL_WOOD_EQUIPMENT_HANDLE,
                    MATERIAL_STONE_EQUIPMENT_HANDLE,
                    MATERIAL_IRON_EQUIPMENT_HANDLE,
                    MATERIAL_DIAMOND_EQUIPMENT_HANDLE,
                    MATERIAL_GOLD_EQUIPMENT_HANDLE,
                    MATERIAL_CHAIN_EQUIPMENT_HANDLE,
                    MATERIAL_LEATHER_EQUIPMENT_HANDLE,
                    MATERIAL_EQUIPMENT_HANDLE,
                    MATERIAL_GENERIC_HANDLE,
                    MATERIAL_LIGHT_HANDLE,
                    MATERIAL_LIQUID_HANDLE
            ).forEach {
                it.registryName = it.soundName
                register(it)
            }
        }
    }
}