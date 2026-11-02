package com.minibusfare.calculator.ui.settings.mvi

sealed interface SettingsEvent {
    data class ShowToast(val message: String) : SettingsEvent
}
