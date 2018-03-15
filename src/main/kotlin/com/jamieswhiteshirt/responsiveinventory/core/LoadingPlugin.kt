package com.jamieswhiteshirt.responsiveinventory.core

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin

@IFMLLoadingPlugin.Name("responsiveinventory")
@IFMLLoadingPlugin.MCVersion("1.11.2")
@IFMLLoadingPlugin.SortingIndex(1004)
@IFMLLoadingPlugin.TransformerExclusions("kotlin", "com.jamieswhiteshirt.responsiveinventory.core")
class LoadingPlugin : IFMLLoadingPlugin {
    override fun getModContainerClass(): String? = null

    override fun getASMTransformerClass(): Array<out String> = arrayOf("com.jamieswhiteshirt.responsiveinventory.core.ClassTransformer")

    override fun getSetupClass(): String? = null

    override fun injectData(data: MutableMap<String, Any>) { }

    override fun getAccessTransformerClass(): String? = null
}