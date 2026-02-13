package com.createcivilization.create_ore_deposits.registry.datamap

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.datamaps.DataMapType

object CreateOreDepositsDataMaps {

	data class HardnessData(val hardness: Float) {
		companion object {
			val CODEC: Codec<HardnessData> = RecordCodecBuilder.create { instance ->
				instance.group(
					Codec.FLOAT.fieldOf("hardness").forGetter(HardnessData::hardness)
				).apply(instance, ::HardnessData)
			}
		}
	}

	data class CoolingFactorData(val coolingFactor: Float) {
		companion object {
			val CODEC: Codec<CoolingFactorData> = RecordCodecBuilder.create { instance ->
				instance.group(
					Codec.FLOAT.fieldOf("cooling_factor").forGetter(CoolingFactorData::coolingFactor)
				).apply(instance, ::CoolingFactorData)
			}
		}
	}

	val HARDNESS_DATA: DataMapType<Block?, HardnessData?> =
		DataMapType.builder(
			ResourceLocation.fromNamespaceAndPath(CreateOreDeposits.MOD_ID, "hardness_data"),
			Registries.BLOCK,
			HardnessData.CODEC
		).build()

	val COOLING_FACTOR_DATA: DataMapType<Fluid, CoolingFactorData> =
		DataMapType.builder(
			ResourceLocation.fromNamespaceAndPath(CreateOreDeposits.MOD_ID, "cooling_factor_data"),
			Registries.FLUID,
			CoolingFactorData.CODEC
		).build()
}
