package com.createcivilization.create_ore_deposits.util

import com.createcivilization.create_ore_deposits.CreateOreDeposits

import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val LOGGER: Logger = LoggerFactory.getLogger(CreateOreDeposits.MOD_ID)

internal fun logI(message: Any?) = LOGGER.info(message.toString())
internal fun logW(message: Any?) = LOGGER.warn(message.toString())

internal fun logE(message: Any?, cause: Throwable? = null) {
	cause?.let { return LOGGER.error(message.toString(), it) }
	LOGGER.error(message.toString())
}

internal fun logD(message: Any?) = LOGGER.debug(message.toString())