package com.createcivilization.create_ore_deposits

import net.createmod.catnip.lang.Lang
import net.createmod.catnip.lang.LangBuilder
import net.createmod.catnip.lang.LangNumberFormat
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack
import kotlin.collections.ArrayList
import kotlin.collections.MutableList


object CreateOreDepositsLang : Lang() {
	fun translateDirect(key: String, vararg args: Any): MutableComponent {
		val args1 = LangBuilder.resolveBuilders(args)
		return Component.translatable(CreateOreDeposits.MOD_ID + key, args1)
	}

	fun translatedOptions(prefix: String, vararg keys: String): MutableList<Component> {
		val result: MutableList<Component> = ArrayList(keys.size)
		for (key in keys) result.add(translate("$prefix.$key").component())
		return result
	}

	//
	fun builder(): LangBuilder {
		return LangBuilder(CreateOreDeposits.MOD_ID)
	}

	fun blockName(state: BlockState): LangBuilder {
		return builder().add(
			state.block
				.name
		)
	}

	fun itemName(stack: ItemStack): LangBuilder {
		return builder().add(
			stack.hoverName
				.copy()
		)
	}

	fun fluidName(stack: FluidStack): LangBuilder {
		return builder().add(
			stack.hoverName
				.copy()
		)
	}

	fun number(d: Double): LangBuilder {
		return builder().text(LangNumberFormat.format(d))
	}

	fun translate(langKey: String, vararg args: Any): LangBuilder {
		return builder().translate(langKey, *args)
	}

	fun text(text: String): LangBuilder {
		return builder().text(text)
	}

	// Use while implementing and replace all references with Lang.translate
	fun temporaryText(text: String): LangBuilder {
		return builder().text(text)
	}
}