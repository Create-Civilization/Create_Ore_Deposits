package com.createcivilization.create_ore_deposits.registry.item

import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.crafting.Ingredient

import java.util.EnumMap

data object CreateOreDepositsArmorMaterials {

	const val STEEL_DURABILITY: Int = 30

	@JvmField
	val STEEL: Holder<ArmorMaterial> = Holder.direct(
		ArmorMaterial(
			EnumMap<ArmorItem.Type, Int>(ArmorItem.Type::class.java).apply {
				put(ArmorItem.Type.BOOTS, 2)
				put(ArmorItem.Type.LEGGINGS, 5)
				put(ArmorItem.Type.CHESTPLATE, 7)
				put(ArmorItem.Type.HELMET, 2)
				put(ArmorItem.Type.BODY, 7)
			},
			9,
			SoundEvents.ARMOR_EQUIP_DIAMOND,
			{ Ingredient.of(CreateOreDepositsItems.STEEL_INGOT.get()) },
			listOf(ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("netherite"))),
			1.5f,
			0.0f
		)
	)
}
