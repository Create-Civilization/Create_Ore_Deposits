package com.createcivilization.create_ore_deposits.registry.datagen

import com.createcivilization.create_ore_deposits.registry.datamap.CreateOreDepositsDataMaps
import com.createcivilization.create_ore_deposits.registry.fluid.CreateOreDepositsFluids
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.tags.FluidTags
import net.neoforged.neoforge.common.data.DataMapProvider
import java.util.concurrent.CompletableFuture

class DataMapProvider(
	packOutput: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>
) : DataMapProvider(packOutput, lookupProvider) {

	override fun gather(provider: HolderLookup.Provider) {
		builder(CreateOreDepositsDataMaps.HARDNESS_DATA)
			.add(BlockTags.DIRT, CreateOreDepositsDataMaps.HardnessData(0.1f), false)


		builder(CreateOreDepositsDataMaps.COOLING_FACTOR_DATA)
			.add(FluidTags.WATER, CreateOreDepositsDataMaps.CoolingFactorData(5f), false)

		builder(CreateOreDepositsDataMaps.LUBRICANT_FACTOR_DATA)
			.add(CreateOreDepositsFluids.LUBRICANT.key, CreateOreDepositsDataMaps.LubricantFactorData(1f), false)
	}
}