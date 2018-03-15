package com.jamieswhiteshirt.responsiveinventory.client

import com.jamieswhiteshirt.responsiveinventory.init.ResponsiveInventorySoundEvents
import net.minecraft.client.audio.ISound
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Slot
import net.minecraft.util.SoundCategory
import net.minecraftforge.fml.relauncher.ReflectionHelper

class DragSplittingSlotsSet(val inner: MutableSet<Slot>, val gui: GuiContainer) : MutableSet<Slot> by inner {
    companion object {
        private val DRAG_SPLITTING_FIELD = ReflectionHelper.findField(GuiContainer::class.java, "dragSplitting", "field_147007_t")
    }

    override fun add(element: Slot): Boolean {
        return if (inner.add(element)) {
            playSound()
            true
        } else {
            false
        }
    }

    override fun addAll(elements: Collection<Slot>): Boolean {
        return if (inner.addAll(elements)) {
            playSound()
            true
        } else {
            false
        }
    }

    override fun remove(element: Slot): Boolean {
        return if (inner.remove(element)) {
            playSound()
            true
        } else {
            false
        }
    }

    private fun playSound() {
        if (DRAG_SPLITTING_FIELD[gui] as Boolean) {
            val player = gui.mc.player
            val stack = player.inventory.itemStack
            if (!stack.isEmpty) {
                val pitch = 0.5F + 1.5F / size
                val soundHandler = gui.mc.soundHandler
                soundHandler.playSound(PositionedSoundRecord(
                        ResponsiveInventorySoundEvents.MATERIAL_GENERIC_HANDLE.soundName, SoundCategory.MASTER,
                        0.5F, pitch, false, 0, ISound.AttenuationType.NONE,
                        player.posX.toFloat(), player.posY.toFloat(), player.posZ.toFloat()
                ))
            }
        }
    }
}

