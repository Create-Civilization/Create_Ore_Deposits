package com.createcivilization.create_ore_deposits.ponder

import com.createcivilization.create_ore_deposits.registry.item.CreateOreDepositsItems

import net.createmod.catnip.math.Pointing
import net.createmod.ponder.api.scene.SceneBuilder
import net.createmod.ponder.api.scene.SceneBuildingUtil
import net.createmod.ponder.api.scene.Selection
import net.minecraft.core.Direction

data object DrillScenes {

	@JvmStatic
	fun scene1(scene: SceneBuilder, util: SceneBuildingUtil) {
		scene.title("deposit_drill_scene_1", "Scene 1")
		scene.configureBasePlate(0, 0, 5)

		scene.world().showSection(util.select().everywhere(), Direction.DOWN)
		scene.addKeyframe()
		scene.idle(20)

		scene.overlay().showText(30)
			.sharedText("drill_scene1_text_1")
			.pointAt(util.vector().of(2.0, 2.5, 2.5))
			.placeNearTarget()
			.attachKeyFrame()
		scene.idle(45)

		scene.overlay().showText(100)
			.sharedText("drill_scene1_text_2")
			.attachKeyFrame()
		scene.idle(10)

		scene.overlay().showText(45)
			.sharedText("drill_scene1_text_3")
			.pointAt(util.vector().of(2.0, 2.5, 2.5))
			.placeNearTarget()
		scene.idle(50)

		scene.rotateCameraY(180f)
		scene.idle(20)

		scene.overlay().showText(60)
			.sharedText("drill_scene1_text_4")
			.pointAt(util.vector().of(3.0, 2.5, 2.5))
			.placeNearTarget()
		scene.idle(60)

		scene.rotateCameraY(180f)
		scene.idle(20)
		scene.markAsFinished()
	}

	@JvmStatic
	fun scene2(scene: SceneBuilder, util: SceneBuildingUtil) {
		scene.title("deposit_drill_scene_2", "Scene 2")
		scene.configureBasePlate(0, 0, 7)
		scene.scaleSceneView(0.72f)

		val baseStructure = util.select().layer(0)
			.add(util.select().position(3, 5, 3))
		val machineAdditions = selectionFromPositions(
			util,
			intArrayOf(1, 5, 3),
			intArrayOf(2, 5, 1),
			intArrayOf(2, 5, 2),
			intArrayOf(2, 5, 3),
			intArrayOf(2, 6, 3),
			intArrayOf(3, 5, 1),
			intArrayOf(3, 5, 2),
			intArrayOf(3, 6, 2),
			intArrayOf(3, 6, 3),
			intArrayOf(3, 7, 2),
			intArrayOf(3, 7, 3)
		)
		val deposit = util.select().fromTo(0, 1, 1, 0, 1, 4)
			.add(util.select().fromTo(1, 1, 0, 1, 1, 5))
			.add(util.select().fromTo(2, 1, 0, 2, 1, 5))
			.add(util.select().fromTo(3, 1, 0, 3, 1, 6))
			.add(util.select().fromTo(4, 1, 1, 4, 1, 5))
			.add(util.select().fromTo(5, 1, 1, 5, 1, 5))
			.add(util.select().fromTo(6, 1, 2, 6, 1, 4))
			.add(util.select().fromTo(1, 2, 2, 1, 2, 3))
			.add(util.select().fromTo(2, 2, 1, 2, 2, 4))
			.add(util.select().fromTo(3, 2, 2, 4, 2, 4))
			.add(util.select().position(5, 2, 3))
		val drillTip = selectionFromPositions(
			util,
			intArrayOf(4, 6, 2),
			intArrayOf(4, 6, 3),
			intArrayOf(5, 5, 3),
			intArrayOf(5, 6, 2),
			intArrayOf(5, 6, 3),
			intArrayOf(5, 6, 4),
			intArrayOf(6, 5, 3),
			intArrayOf(6, 5, 4),
			intArrayOf(6, 6, 4),
			intArrayOf(7, 5, 4),
			intArrayOf(8, 5, 4)
		)

		scene.world().showSection(baseStructure, Direction.DOWN)
		scene.idle(30)

		scene.world().showSection(machineAdditions, Direction.UP)
		scene.idle(20)

		scene.rotateCameraY(180f)
		scene.idle(20)

		scene.overlay().showText(70)
			.sharedText("drill_scene2_text_1")
		scene.idle(75)

		scene.overlay().showText(60)
			.sharedText("drill_scene2_text_2")
			.pointAt(util.vector().of(2.0, 3.2, -1.2))
			.placeNearTarget()

		scene.overlay().showText(60)
			.sharedText("drill_scene2_text_3")
			.pointAt(util.vector().of(2.0, 5.15, 0.7))
			.placeNearTarget()
		scene.idle(60)

		scene.world().showSection(deposit, Direction.EAST)
		scene.idle(20)

		scene.overlay().showText(60)
			.sharedText("drill_scene2_text_4")
			.pointAt(util.vector().of(4.0, 1.5, 4.5))
			.placeNearTarget()

		scene.world().showSection(drillTip, Direction.EAST)
		scene.idle(20)

		scene.overlay().showControls(util.vector().of(3.3, 1.5, 2.9), Pointing.LEFT, 45)
			.rightClick()
			.withItem(CreateOreDepositsItems.DIAMOND_DRILL_TIP.asStack())
		scene.idle(10)
		scene.markAsFinished()
	}

	private fun selectionFromPositions(util: SceneBuildingUtil, vararg positions: IntArray): Selection {
		var selection = util.select().position(positions[0][0], positions[0][1], positions[0][2])
		for (index in 1 until positions.size) {
			val position = positions[index]
			selection = selection.add(util.select().position(position[0], position[1], position[2]))
		}
		return selection
	}
}
