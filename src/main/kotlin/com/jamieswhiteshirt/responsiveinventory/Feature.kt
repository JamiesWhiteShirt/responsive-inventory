package com.jamieswhiteshirt.responsiveinventory

import net.minecraftforge.common.config.Configuration
import java.util.*

interface IFeature {
    val featureName: String

    fun register()

    fun registerRecipes()

    fun registerEventHandlers()
}

interface ISubFeature : IFeature {
    val subFeatureName: String

    val parent: IFeature
}

abstract class SubFeature(override val subFeatureName: String, override val parent: IFeature) : ISubFeature {
    override val featureName: String get() = "${parent.featureName}.$subFeatureName"

    override fun register() {}

    override fun registerRecipes() {}

    override fun registerEventHandlers() {}
}

abstract class FeatureCollectionBase<T : ISubFeature> : IFeature {
    private val _subFeatures = ArrayList<T>()

    val subFeatures: List<T> get() = _subFeatures

    protected fun <U : T> optionalOn(config: Configuration, feature: U?): U? {
        return optional(config, feature, true)
    }

    protected fun <U : T> optionalOff(config: Configuration, feature: U?): U? {
        return optional(config, feature, false)
    }

    protected fun <U: T> optional(config: Configuration, feature: U?, default: Boolean): U? {
        if (feature != null) {
            if (config.getBoolean("enable", feature.featureName, default, "Enable this feature")) {
                return required(feature)
            }
        }
        return null
    }

    protected fun <U : T> required(feature: U): U {
        _subFeatures.add(feature)
        return feature
    }

    override fun register() = subFeatures.forEach(IFeature::register)

    override fun registerEventHandlers() = subFeatures.forEach(IFeature::registerEventHandlers)
}

abstract class SubFeatureCollection<T : ISubFeature>(override val subFeatureName: String, override val parent: IFeature) : FeatureCollectionBase<T>(), ISubFeature {
    override val featureName: String get() = "${parent.featureName}.$subFeatureName"
}
