package com.jamieswhiteshirt.responsiveinventory.client

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiIngame
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.*

object RenderGameOverlayHandler : GuiIngame(Minecraft.getMinecraft()) {
    private val RI_WIDGETS_TEX_PATH = ResourceLocation("responsiveinventory", "textures/gui/widgets.png")

    @SubscribeEvent
    fun onRenderGameOverlay(e: RenderGameOverlayEvent.Post) {
        if (e.type == RenderGameOverlayEvent.ElementType.HOTBAR) {
            /* val player = mc.renderViewEntity as EntityPlayer
            val currentItem = player.inventory.currentItem
            val currentStack = player.inventory.mainInventory[currentItem]
            if (!currentStack.isEmpty) {
                val otherStack = player.inventory.mainInventory.filterIndexed { index, itemStack ->
                    index != currentItem && ItemStack.areItemsEqual(currentStack, itemStack)
                }.firstOrNull()
                if (otherStack != null) {
                    val partialTicks = e.partialTicks
                    val sr = e.resolution
                    val i = sr.scaledWidth / 2
                    val l = player.inventory.currentItem
                    val i1 = i - 90 + l * 20 + 2 + 2
                    val j1 = sr.scaledHeight - 16 - 3 - 4

                    mc.textureManager.bindTexture(WIDGETS_TEX_PATH)
                    GlStateManager.enableAlpha()
                    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO)
                    GlStateManager.color(0.5F, 0.5F, 0.5F)
                    drawTexturedModalRect(i - 91 - 1 + currentItem * 20 + 2, sr.scaledHeight - 22 - 1 - 4, 0, 22, 24, 24)

                    GlStateManager.enableRescaleNormal()
                    RenderHelper.enableGUIStandardItemLighting()

                    zLevel = -180.0F
                    renderHotbarItem(i1, j1, partialTicks, player, otherStack)

                    RenderHelper.disableStandardItemLighting()
                    GlStateManager.disableRescaleNormal()
                    GlStateManager.disableAlpha()
                }
            } */

            val sr = e.resolution
            val i = sr.scaledWidth / 2

            zLevel = -90.0F

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F)
            mc.textureManager.bindTexture(RI_WIDGETS_TEX_PATH)
            GlStateManager.enableAlpha()
            GlStateManager.enableBlend()
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO)

            for (item in 0..1) {
                drawTexturedModalRect(i - 91 + item * 20, sr.scaledHeight - 22, 0, 0, 22, 22)
            }

            GlStateManager.disableBlend()
            GlStateManager.disableAlpha()
        }
    }

    private class Particle(
        var posX: Float,
        var posY: Float,
        var motionX: Float,
        var motionY: Float,
        var jitterX: Float,
        var jitterY: Float,
        var ticksLeft: Int,
        val icon: TextureAtlasSprite
    ) {
        fun tick(): Boolean {
            posX += motionX
            posY += motionY
            motionY += 0.5F
            ticksLeft--
            return ticksLeft <= 0
        }
    }

    private var prevX: Int = 0
    private var prevY: Int = 0
    private var prevDx: Int = 0
    private var prevDy: Int = 0
    private val random = Random()
    private val particles: MutableList<Particle> = LinkedList()

    fun spawnParticle(posX: Float, posY: Float, motionX: Float, motionY: Float, item: Item, meta: Int) {
        val itemModelMesher = Minecraft.getMinecraft().renderItem.itemModelMesher
        val icon = itemModelMesher.getParticleIcon(item, meta)
        if (icon != null) {
            particles.add(Particle(
                posX,
                posY,
                (random.nextFloat() - random.nextFloat()) * 3.0F + motionX,
                (random.nextFloat() - random.nextFloat()) * 3.0F + motionY,
                random.nextFloat() * 12.0F,
                random.nextFloat() * 12.0F,
                200,
                icon
            ))
        }
    }

    @SubscribeEvent
    fun onBackgroundDrawn(e: GuiScreenEvent.DrawScreenEvent.Post) {
        val dx = e.mouseX - prevX
        val dy = e.mouseY - prevY
        val ddx = dx - prevDx
        val ddy = dy - prevDy

        val player = Minecraft.getMinecraft().player
        if (player != null) {
            val heldStack = player.inventory.itemStack
            if (!heldStack.isEmpty && random.nextInt(1 + 500 / (1 + heldStack.count * (ddx * ddx + ddy * ddy))) == 0) {
                spawnParticle(
                    e.mouseX.toFloat(),
                    e.mouseY.toFloat(),
                    dx.toFloat(),
                    dy.toFloat(),
                    heldStack.item,
                    heldStack.metadata
                )
            }
        }

        prevX = e.mouseX
        prevY = e.mouseY
        prevDx = dx
        prevDy = dy

        particles.removeIf(Particle::tick)

        this.zLevel = 0.0F
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F)
        mc.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
        GlStateManager.enableAlpha()
        GlStateManager.enableBlend()

        val tessellator = Tessellator.getInstance()
        val vertexbuffer = tessellator.getBuffer()
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX)

        particles.forEach {
            val icon = it.icon
            vertexbuffer.pos(it.posX.toDouble() - 2.0, it.posY.toDouble() + 2.0, zLevel.toDouble()).tex(icon.getInterpolatedU(it.jitterX.toDouble()).toDouble(), icon.getInterpolatedV(it.jitterY.toDouble() + 4.0).toDouble()).endVertex()
            vertexbuffer.pos(it.posX.toDouble() + 2.0, it.posY.toDouble() + 2.0, zLevel.toDouble()).tex(icon.getInterpolatedU(it.jitterX.toDouble() + 4.0).toDouble(), icon.getInterpolatedV(it.jitterY.toDouble() + 4.0).toDouble()).endVertex()
            vertexbuffer.pos(it.posX.toDouble() + 2.0, it.posY.toDouble() - 2.0, zLevel.toDouble()).tex(icon.getInterpolatedU(it.jitterX.toDouble() + 4.0).toDouble(), icon.getInterpolatedV(it.jitterY.toDouble()).toDouble()).endVertex()
            vertexbuffer.pos(it.posX.toDouble() - 2.0, it.posY.toDouble() - 2.0, zLevel.toDouble()).tex(icon.getInterpolatedU(it.jitterX.toDouble()).toDouble(), icon.getInterpolatedV(it.jitterY.toDouble()).toDouble()).endVertex()
        }

        tessellator.draw()

        GlStateManager.disableBlend()
        GlStateManager.disableAlpha()
    }
}
