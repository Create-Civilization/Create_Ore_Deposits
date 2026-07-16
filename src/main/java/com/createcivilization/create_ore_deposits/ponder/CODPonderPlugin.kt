package com.createcivilization.create_ore_deposits.ponder

import com.createcivilization.create_ore_deposits.CreateOreDeposits
import com.createcivilization.create_ore_deposits.registry.block.CreateOreDepositsBlocks
import com.tterrag.registrate.util.entry.ItemProviderEntry

import net.createmod.ponder.api.registration.PonderPlugin
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper
import net.createmod.ponder.api.registration.SharedTextRegistrationHelper
import net.minecraft.resources.ResourceLocation

data object CODPonderPlugin : PonderPlugin {

	override fun getModId(): String = CreateOreDeposits.MOD_ID

	override fun registerScenes(helper: PonderSceneRegistrationHelper<ResourceLocation>) {
		val sceneHelper = helper.withKeyFunction<ItemProviderEntry<*, *>> { entry -> entry.id }

		sceneHelper.forComponents(CreateOreDepositsBlocks.DRILL_BLOCK)
			.addStoryBoard("drill/scene1", DrillScenes::scene1)
			.addStoryBoard("drill/scene2", DrillScenes::scene2)
	}

	override fun registerSharedText(helper: SharedTextRegistrationHelper) {
		helper.registerSharedText("drill_scene1_text_1", "This is the Deposit Drill, it drills.")
		helper.registerSharedText("drill_scene1_text_2", "It has 3 inputs and 1 output")
		helper.registerSharedText("drill_scene1_text_3", "1 Rotational Input and 2 Fluid Inputs")
		helper.registerSharedText("drill_scene1_text_4", "...and an Item Output.")
		helper.registerSharedText("drill_scene2_text_1", "Some fluids can be pumped into the drill to slow the process of the drill overheating")
		helper.registerSharedText("drill_scene2_text_2", "Coolant")
		helper.registerSharedText("drill_scene2_text_3", "Lubricant")
		helper.registerSharedText("drill_scene2_text_4", "This is a deposit. This is what the Drill drills")
	}
}
