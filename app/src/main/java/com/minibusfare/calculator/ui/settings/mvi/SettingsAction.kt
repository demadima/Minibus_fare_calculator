package com.minibusfare.calculator.ui.settings.mvi

sealed interface SettingsAction {
    data class OnCityAdultPriceChanged(val value: String) : SettingsAction
    data class OnCityChildPriceChanged(val value: String) : SettingsAction
    data class OnVillageAdultPriceChanged(val value: String) : SettingsAction
    data class OnVillageChildPriceChanged(val value: String) : SettingsAction
    data class OnPresetChanged(val index: Int, val value: String) : SettingsAction
    object OnSaveClicked : SettingsAction
    object OnResetToDefaultsClicked : SettingsAction
}
