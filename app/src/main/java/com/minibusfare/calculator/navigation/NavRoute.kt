package com.minibusfare.calculator.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavRoute

@Serializable
object CalculatorRoute : NavRoute

@Serializable
object SettingsRoute : NavRoute
