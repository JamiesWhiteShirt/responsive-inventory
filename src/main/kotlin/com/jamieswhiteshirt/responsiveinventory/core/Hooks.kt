package com.jamieswhiteshirt.responsiveinventory.core

import com.jamieswhiteshirt.responsiveinventory.client.DragSplittingSlotsSet
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Slot

object Hooks {
    @JvmStatic
    fun newDragSplittingSlotsSet(set: MutableSet<Slot>, gui: GuiContainer): MutableSet<Slot> {
        return DragSplittingSlotsSet(set, gui)
    }
}
