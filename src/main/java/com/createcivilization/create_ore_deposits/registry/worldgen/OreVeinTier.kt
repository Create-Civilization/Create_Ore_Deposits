package com.createcivilization.create_ore_deposits.registry.worldgen

import com.mojang.serialization.Codec
import net.minecraft.util.StringRepresentable

enum class OreVeinTier(private val serializedName: String) : StringRepresentable {
	LARGE("large"),
	MEDIUM("medium"),
	SMALL("small");

	override fun getSerializedName(): String = serializedName

	companion object {

		@JvmField
		val CODEC: Codec<OreVeinTier> = Codec.STRING.xmap(::byName, OreVeinTier::getSerializedName)

		// Keep unknown strings on the smallest tier so old generated data remains readable instead of hard-failing on rename drift.
		fun byName(name: String): OreVeinTier = entries.firstOrNull { it.serializedName == name } ?: SMALL
	}
}
