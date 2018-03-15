package com.jamieswhiteshirt.responsiveinventory.core

import net.minecraft.launchwrapper.IClassTransformer
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*

class ClassTransformer : IClassTransformer {
    override fun transform(
        name: String,
        transformedName: String,
        basicClass: ByteArray
    ): ByteArray = when (transformedName) {
        "net.minecraft.client.gui.inventory.GuiContainer" -> transformSingleMethod(
            basicClass,
            "<init>",
            "<init>",
            "(Lnet/minecraft/inventory/Container;)V"
        ) { methodNode ->
            for (insn in methodNode.instructions) {
                if (
                    insn is FieldInsnNode &&
                    insn.opcode == Opcodes.PUTFIELD &&
                    insn.owner == "net/minecraft/client/gui/inventory/GuiContainer" &&
                    insn.name.equalsEither("field_147008_s", "dragSplittingSlots") &&
                    insn.desc == "Ljava/util/Set;"
                ) {
                    methodNode.instructions.insertBefore(insn, InsnList().apply {
                        add(VarInsnNode(Opcodes.ALOAD, 0))
                        add(MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            "com/jamieswhiteshirt/responsiveinventory/core/Hooks",
                            "newDragSplittingSlotsSet",
                            "(Ljava/util/Set;Lnet/minecraft/client/gui/inventory/GuiContainer;)Ljava/util/Set;",
                            false
                        ))
                    })
                }
            }
        }
        else -> basicClass
    }

    private fun String.equalsEither(srgName: String, mcpName: String) = this == srgName || this == mcpName

    private inline fun transformClass(basicClass: ByteArray, transformer: (ClassNode) -> Unit): ByteArray {
        val reader = ClassReader(basicClass)
        val classNode = ClassNode()
        reader.accept(classNode, 0)
        transformer(classNode)
        val writer = ClassWriter(ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS)
        classNode.accept(writer)
        return writer.toByteArray()
    }

    private inline fun transformSingleMethod(
        basicClass: ByteArray,
        srgName: String,
        mcpName: String,
        desc: String,
        transformer: (MethodNode) -> Unit
    ): ByteArray = transformClass(basicClass) { classNode ->
        classNode.methods.find { it.name.equalsEither(srgName, mcpName) && it.desc == desc }?.let {
            transformer(it)
        }
    }
}