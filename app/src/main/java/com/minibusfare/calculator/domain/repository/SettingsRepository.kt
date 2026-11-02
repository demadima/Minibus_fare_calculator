package com.minibusfare.calculator.domain.repository

import com.minibusfare.calculator.domain.model.FareSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val fareSettingsFlow: Flow<FareSettings>
    suspend fun saveFareSettings(settings: FareSettings)
    suspend fun resetToDefaults()
}
