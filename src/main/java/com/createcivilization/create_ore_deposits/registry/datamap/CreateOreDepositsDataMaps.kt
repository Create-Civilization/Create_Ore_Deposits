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

	data class DepositData(val maxAttempts: Int, val hardness: Float) {
		companion object {
			val CODEC: Codec<DepositData> = RecordCodecBuilder.create { instance ->
				instance.group(
					Codec.INT.fieldOf("maxAttempts").forGetter(DepositData::maxAttempts),
					Codec.FLOAT.fieldOf("hardness").forGetter(DepositData::hardness)
				).apply(instance, ::DepositData)
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

	data class LubricantFactorData(val lubeFactor: Float) {
		companion object {
			val CODEC: Codec<LubricantFactorData> = RecordCodecBuilder.create { instance ->
				instance.group(
					Codec.FLOAT.fieldOf("lube_factor").forGetter(LubricantFactorData::lubeFactor)
				).apply(instance, ::LubricantFactorData)
			}
		}
	}

	val DEPOSIT_DATA: DataMapType<Block?, DepositData?> =
		DataMapType.builder(
			ResourceLocation.fromNamespaceAndPath(CreateOreDeposits.MOD_ID, "deposit_data"),
			Registries.BLOCK,
			DepositData.CODEC
		).build()

	val COOLING_FACTOR_DATA: DataMapType<Fluid, CoolingFactorData> =
		DataMapType.builder(
			ResourceLocation.fromNamespaceAndPath(CreateOreDeposits.MOD_ID, "cooling_factor_data"),
			Registries.FLUID,
			CoolingFactorData.CODEC
		).build()


	val LUBRICANT_FACTOR_DATA: DataMapType<Fluid, LubricantFactorData> =
		DataMapType.builder(
			ResourceLocation.fromNamespaceAndPath(CreateOreDeposits.MOD_ID, "lubricant_factor_data"),
			Registries.FLUID,
			LubricantFactorData.CODEC
		).build()
}
