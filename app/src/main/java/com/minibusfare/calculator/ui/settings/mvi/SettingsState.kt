package com.minibusfare.calculator.ui.settings.mvi

data class SettingsState(
    val cityAdultPriceInput: String = "15",
    val cityChildPriceInput: String = "8",
    val villageAdultPriceInput: String = "28",
    val villageChildPriceInput: String = "15",
    val presetInputs: List<String> = listOf("20", "40", "50", "60", "70", "80", "90", "100", "120", "140", "150", "160", "200", "500", "1000", "2000"),
    val isSavedSuccess: Boolean = false
)
