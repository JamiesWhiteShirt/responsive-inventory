package com.jamieswhiteshirt.responsiveinventory.client

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.jamieswhiteshirt.responsiveinventory.init.ResponsiveInventorySoundEvents
import net.minecraft.block.SoundType
import net.minecraft.client.resources.IResourceManager
import net.minecraft.client.resources.IResourceManagerReloadListener
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraftforge.fml.common.registry.ForgeRegistries
import org.apache.commons.io.IOUtils
import org.apache.logging.log4j.LogManager
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object ItemSoundHandler : IResourceManagerReloadListener {
    private val LOGGER = LogManager.getLogger()
    private val GSON = Gson()
    private val TYPE = (object : TypeToken<Map<String, String>>() {}).type
    private val REGISTRY = ItemSoundRegistry()

    override fun onResourceManagerReload(resourceManager: IResourceManager) {
        REGISTRY.clearMap()

        ForgeRegistries.ITEMS.values.filterIsInstance<ItemBlock>().forEach {
            val soundEvent = when (it.getBlock().soundType) {
                SoundType.WOOD -> ResponsiveInventorySoundEvents.MATERIAL_WOODEN_HANDLE
                SoundType.GROUND -> ResponsiveInventorySoundEvents.MATERIAL_COARSE_HANDLE
                SoundType.PLANT -> ResponsiveInventorySoundEvents.MATERIAL_PLANT_HANDLE
                SoundType.STONE -> ResponsiveInventorySoundEvents.MATERIAL_SOLID_HANDLE
                SoundType.METAL -> ResponsiveInventorySoundEvents.MATERIAL_METALLIC_HANDLE
                SoundType.GLASS -> ResponsiveInventorySoundEvents.MATERIAL_FRAGILE_HANDLE
                SoundType.CLOTH -> ResponsiveInventorySoundEvents.MATERIAL_SOFT_HANDLE
                SoundType.SAND -> ResponsiveInventorySoundEvents.MATERIAL_FINE_HANDLE
                SoundType.SNOW -> ResponsiveInventorySoundEvents.MATERIAL_SNOWY_HANDLE
                SoundType.LADDER -> ResponsiveInventorySoundEvents.MATERIAL_WOODEN_HANDLE
                SoundType.ANVIL -> ResponsiveInventorySoundEvents.MATERIAL_HEAVY_METALLIC_HANDLE
                SoundType.SLIME -> ResponsiveInventorySoundEvents.MATERIAL_STICKY_HANDLE
                else -> null
            }

            if (soundEvent != null) {
                REGISTRY.putObject(it.registryName, soundEvent)
            }
        }

        for (domain in resourceManager.resourceDomains) {
            try {
                resourceManager.getAllResources(ResourceLocation(domain, "item_sounds.json"))
                        .mapNotNull {
                            try {
                                getItemSoundMap(it.inputStream)
                            } catch (e: RuntimeException) {
                                LOGGER.warn("Invalid item_sounds.json", e)
                                null
                            }
                        }
                        .forEach {
                            for ((key, value) in it) {
                                val itemLocation = ResourceLocation(domain, key)
                                val soundEventLocation = ResourceLocation(value)
                                val soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(soundEventLocation)
                                if (soundEvent != null) {
                                    val item = ForgeRegistries.ITEMS.getValue(itemLocation)
                                    // Ignore for unregistered items
                                    if (item != null) {
                                        REGISTRY.putObject(itemLocation, soundEvent)
                                    }
                                } else {
                                    LOGGER.warn("Invalid sound event $soundEventLocation for $itemLocation")
                                }
                            }
                        }
            } catch (e: IOException) { }
        }
    }

    private fun getItemSoundMap(stream: InputStream): Map<String, String> {
        return try {
            GSON.fromJson(InputStreamReader(stream), TYPE)
        } catch (e: IOException) {
            throw e
        } finally {
            IOUtils.closeQuietly(stream)
        }
    }

    fun getSoundEvent(item: Item): SoundEvent? = REGISTRY.getObject(item.registryName)
}