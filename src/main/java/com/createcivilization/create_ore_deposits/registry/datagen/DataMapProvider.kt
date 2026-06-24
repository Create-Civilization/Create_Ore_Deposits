package com.createcivilization.create_ore_deposits.registry.datagen

import com.createcivilization.create_ore_deposits.registry.block.CreateOreDepositsBlocks
import com.createcivilization.create_ore_deposits.registry.datamap.CreateOreDepositsDataMaps
import com.createcivilization.create_ore_deposits.registry.fluid.CreateOreDepositsFluids
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.FluidTags
import net.neoforged.neoforge.common.data.DataMapProvider
import java.util.concurrent.CompletableFuture

class DataMapProvider(
	packOutput: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>
) : DataMapProvider(packOutput, lookupProvider) {

	override fun gather(provider: HolderLookup.Provider) {
		builder(CreateOreDepositsDataMaps.DEPOSIT_DATA)
			.add(CreateOreDepositsBlocks.EXAMPLE_DEPOSIT, CreateOreDepositsDataMaps.DepositData(10, 1f), false)
			.add(CreateOreDepositsBlocks.COAL_ORE_DEPOSIT, CreateOreDepositsDataMaps.DepositData(1000, 1f), false)
			.add(CreateOreDepositsBlocks.IRON_ORE_DEPOSIT, CreateOreDepositsDataMaps.DepositData(700, 2f), false)
			.add(CreateOreDepositsBlocks.GOLD_ORE_DEPOSIT, CreateOreDepositsDataMaps.DepositData(300, 3f), false)
			.add(CreateOreDepositsBlocks.COPPER_ORE_DEPOSIT, CreateOreDepositsDataMaps.DepositData(800, 1f), false)
			.add(CreateOreDepositsBlocks.LAPIS_ORE_DEPOSIT, CreateOreDepositsDataMaps.DepositData(400, 2f), false)
			.add(CreateOreDepositsBlocks.DIAMOND_ORE_DEPOSIT, CreateOreDepositsDataMaps.DepositData(50, 5f), false)
			.add(CreateOreDepositsBlocks.EMERALD_ORE_DEPOSIT, CreateOreDepositsDataMaps.DepositData(30, 3f), false)
			.add(CreateOreDepositsBlocks.QUARTZ_ORE_DEPOSIT, CreateOreDepositsDataMaps.DepositData(400, 2f), false)
			.add(CreateOreDepositsBlocks.NETHERITE_ORE_DEPOSIT, CreateOreDepositsDataMaps.DepositData(40, 7f), false)

		builder(CreateOreDepositsDataMaps.COOLING_FACTOR_DATA)
			.add(FluidTags.WATER, CreateOreDepositsDataMaps.CoolingFactorData(1f), false)

		builder(CreateOreDepositsDataMaps.LUBRICANT_FACTOR_DATA)
			.add(CreateOreDepositsFluids.LUBRICANT.get().source.builtInRegistryHolder(), CreateOreDepositsDataMaps.LubricantFactorData(1f), false)
	}
}