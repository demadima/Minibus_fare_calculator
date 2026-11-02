package com.minibusfare.calculator.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minibusfare.calculator.domain.model.FareSettings
import com.minibusfare.calculator.domain.repository.SettingsRepository
import com.minibusfare.calculator.ui.settings.mvi.SettingsAction
import com.minibusfare.calculator.ui.settings.mvi.SettingsEvent
import com.minibusfare.calculator.ui.settings.mvi.SettingsState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    private val _event = Channel<SettingsEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            val current = settingsRepository.fareSettingsFlow.first()
            _state.value = SettingsState(
                cityAdultPriceInput = current.cityAdultPrice.toString(),
                cityChildPriceInput = current.cityChildPrice.toString(),
                villageAdultPriceInput = current.villageAdultPrice.toString(),
                villageChildPriceInput = current.villageChildPrice.toString(),
                presetInputs = current.presetAmounts.map { it.toString() }
            )
        }
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.OnCityAdultPriceChanged -> {
                _state.update { it.copy(cityAdultPriceInput = action.value.filter { char -> char.isDigit() }) }
            }
            is SettingsAction.OnCityChildPriceChanged -> {
                _state.update { it.copy(cityChildPriceInput = action.value.filter { char -> char.isDigit() }) }
            }
            is SettingsAction.OnVillageAdultPriceChanged -> {
                _state.update { it.copy(villageAdultPriceInput = action.value.filter { char -> char.isDigit() }) }
            }
            is SettingsAction.OnVillageChildPriceChanged -> {
                _state.update { it.copy(villageChildPriceInput = action.value.filter { char -> char.isDigit() }) }
            }
            is SettingsAction.OnPresetChanged -> {
                val updated = _state.value.presetInputs.toMutableList()
                if (action.index in updated.indices) {
                    updated[action.index] = action.value.filter { char -> char.isDigit() }
                    _state.update { it.copy(presetInputs = updated) }
                }
            }
            is SettingsAction.OnSaveClicked -> {
                saveSettings()
            }
            is SettingsAction.OnResetToDefaultsClicked -> {
                resetToDefaults()
            }
        }
    }

    private fun saveSettings() {
        viewModelScope.launch {
            val current = _state.value

            val cityAdult = current.cityAdultPriceInput.toIntOrNull() ?: FareSettings.DEFAULT_CITY_ADULT_PRICE
            val cityChild = current.cityChildPriceInput.toIntOrNull() ?: FareSettings.DEFAULT_CITY_CHILD_PRICE
            val villageAdult = current.villageAdultPriceInput.toIntOrNull() ?: FareSettings.DEFAULT_VILLAGE_ADULT_PRICE
            val villageChild = current.villageChildPriceInput.toIntOrNull() ?: FareSettings.DEFAULT_VILLAGE_CHILD_PRICE

            val presets = current.presetInputs.mapIndexed { index, input ->
                input.toIntOrNull() ?: FareSettings.DEFAULT_PRESETS.getOrElse(index) { 10 }
            }

            val newSettings = FareSettings(
                cityAdultPrice = cityAdult,
                cityChildPrice = cityChild,
                villageAdultPrice = villageAdult,
                villageChildPrice = villageChild,
                presetAmounts = presets
            )

            settingsRepository.saveFareSettings(newSettings)
            _event.send(SettingsEvent.ShowToast("Налаштування збережено"))
        }
    }

    private fun resetToDefaults() {
        viewModelScope.launch {
            settingsRepository.resetToDefaults()
            val defaultSettings = FareSettings()
            _state.value = SettingsState(
                cityAdultPriceInput = defaultSettings.cityAdultPrice.toString(),
                cityChildPriceInput = defaultSettings.cityChildPrice.toString(),
                villageAdultPriceInput = defaultSettings.villageAdultPrice.toString(),
                villageChildPriceInput = defaultSettings.villageChildPrice.toString(),
                presetInputs = defaultSettings.presetAmounts.map { it.toString() }
            )
            _event.send(SettingsEvent.ShowToast("Скинуто до заводських налаштувань"))
        }
    }
}
