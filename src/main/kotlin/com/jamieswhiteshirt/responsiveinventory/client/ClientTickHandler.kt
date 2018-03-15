package com.jamieswhiteshirt.responsiveinventory.client

import com.jamieswhiteshirt.responsiveinventory.client.itemdiff.diff.EditScriptOperation
import com.jamieswhiteshirt.responsiveinventory.client.itemdiff.diff.diffPlayer
import com.jamieswhiteshirt.responsiveinventory.client.itemdiff.state.PlayerState
import com.jamieswhiteshirt.responsiveinventory.client.itemdiff.reducePlayerDiff
import com.jamieswhiteshirt.responsiveinventory.client.itemdiff.state.ContainerState
import com.jamieswhiteshirt.responsiveinventory.client.itemdiff.state.PlayerInventoryState
import net.minecraft.client.Minecraft
import net.minecraft.client.audio.ISound
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.util.SoundCategory
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import kotlin.math.roundToInt

object ClientTickHandler {
    var playerState: PlayerState? = null

    @SubscribeEvent
    fun onClientTick(e: TickEvent.ClientTickEvent) {
        if (e.phase == TickEvent.Phase.END) {
            val player = Minecraft.getMinecraft().player
            val gui = Minecraft.getMinecraft().currentScreen as? GuiContainer

            val before = playerState
            if (player != null) {
                val after = PlayerState(PlayerInventoryState(player.inventory), gui?.inventorySlots?.let(::ContainerState))
                if (before != null) {
                    val diff = diffPlayer(before, after)

                    diff.openContainer?.let { containerDiff ->
                        if (gui != null) {
                            for ((slot, operation) in containerDiff.inventorySlots) {
                                if (operation != EditScriptOperation.KEEP) {
                                    val stack = slot.stack
                                    (0 until Math.sqrt(stack.count.toDouble()).roundToInt()).forEach {
                                        RenderGameOverlayHandler.spawnParticle(
                                            (gui.guiLeft + slot.xPos + 8).toFloat(),
                                            (gui.guiTop + slot.yPos + 8).toFloat(),
                                            0.0F,
                                            0.0F,
                                            stack.discriminator.item,
                                            stack.discriminator.itemDamage
                                        )
                                    }
                                }
                            }
                        }
                    }

                    val reduction = reducePlayerDiff(diff)

                    reduction.mapNotNull { (item, _) ->
                        ItemSoundHandler.getSoundEvent(item)
                    }.toSet().forEach { soundEvent ->
                        val volume = 0.5F
                        val soundHandler = Minecraft.getMinecraft().soundHandler
                        soundHandler.playSound(PositionedSoundRecord(
                            soundEvent.soundName, SoundCategory.MASTER,
                            volume, 2.0F, false, 0, ISound.AttenuationType.NONE,
                            player.posX.toFloat(), player.posY.toFloat(), player.posZ.toFloat()
                        ))
                    }
                }
                playerState = after
            } else {
                playerState = null
            }
        }
    }
}
