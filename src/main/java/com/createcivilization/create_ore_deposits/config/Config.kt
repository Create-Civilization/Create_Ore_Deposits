@file:Suppress("PropertyName")
package com.createcivilization.create_ore_deposits.config

import net.neoforged.neoforge.common.ModConfigSpec

data object Config {

	class Server(builder: ModConfigSpec.Builder) {

		val DEPOSIT_DRILL: DepositDrill = run {
			builder.push("deposit_drill")
			val config = DepositDrill(builder)
			builder.pop()
			return@run config
		}
		class DepositDrill(builder: ModConfigSpec.Builder) {

			@PublishedApi
			internal val _baseCooling: ModConfigSpec.DoubleValue = builder.defineInRange("baseCooling", 0.03, 0.0, Double.MAX_VALUE)
			inline val baseCooling: Float get() = _baseCooling.get().toFloat()

			@PublishedApi
			internal val _baseTemperature: ModConfigSpec.DoubleValue =
				builder.defineInRange("baseTemperature", 300.0, -Double.MAX_VALUE, Double.MAX_VALUE)
			inline val baseTemperature: Float get() = _baseTemperature.get().toFloat()

			@PublishedApi
			internal val _dampening: ModConfigSpec.DoubleValue = builder.defineInRange("dampening", 0.05, 0.0, Double.MAX_VALUE)
			inline val dampening: Float get() = _dampening.get().toFloat()

			@PublishedApi
			internal val _scale: ModConfigSpec.DoubleValue = builder.defineInRange("scale", 1.5, 0.0, Double.MAX_VALUE)
			inline val scale: Float get() = _scale.get().toFloat()
		}
	}

	@JvmField val SERVER: Server
	internal val serverSpec: ModConfigSpec

	init {
		ModConfigSpec.Builder().configure(::Server).let {
			SERVER = it.left
			serverSpec = it.right
		}
	}
}