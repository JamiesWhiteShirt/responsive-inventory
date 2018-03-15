package com.jamieswhiteshirt.responsiveinventory

import com.jamieswhiteshirt.responsiveinventory.client.ClientTickHandler
import com.jamieswhiteshirt.responsiveinventory.client.ItemSoundHandler
import com.jamieswhiteshirt.responsiveinventory.init.ResponsiveInventorySoundEvents
import com.jamieswhiteshirt.responsiveinventory.client.RenderGameOverlayHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.IReloadableResourceManager
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(
        modid = ResponsiveInventory.MODID,
        name = ResponsiveInventory.NAME,
        version = ResponsiveInventory.VERSION,
        acceptedMinecraftVersions = ResponsiveInventory.ACCEPTED_MINECRAFT_VERSIONS,
        modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter",
        clientSideOnly = true
)
object ResponsiveInventory {
    const val MODID = "responsiveinventory"
    const val NAME = "Responsive Inventory"
    const val VERSION = "1.11.2-1.0.0.0-SNAPSHOT"
    const val ACCEPTED_MINECRAFT_VERSIONS = "[1.11.2]"

    init {
        MinecraftForge.EVENT_BUS.register(ResponsiveInventorySoundEvents)
    }

    @Mod.EventHandler
    fun init(@Suppress("UNUSED_PARAMETER") event: FMLInitializationEvent) {
        (Minecraft.getMinecraft().resourceManager as IReloadableResourceManager).registerReloadListener(ItemSoundHandler)

        MinecraftForge.EVENT_BUS.register(ClientTickHandler)
        MinecraftForge.EVENT_BUS.register(RenderGameOverlayHandler)
    }
}
