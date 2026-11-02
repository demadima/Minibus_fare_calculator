package com.minibusfare.calculator.data.repository

import com.minibusfare.calculator.data.source.datastore.DataStoreManager
import com.minibusfare.calculator.data.source.datastore.ds_const.KEY_CITY_ADULT_PRICE
import com.minibusfare.calculator.data.source.datastore.ds_const.KEY_CITY_CHILD_PRICE
import com.minibusfare.calculator.data.source.datastore.ds_const.KEY_PRESET_AMOUNTS
import com.minibusfare.calculator.data.source.datastore.ds_const.KEY_VILLAGE_ADULT_PRICE
import com.minibusfare.calculator.data.source.datastore.ds_const.KEY_VILLAGE_CHILD_PRICE
import com.minibusfare.calculator.domain.model.FareSettings
import com.minibusfare.calculator.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class SettingsRepositoryImpl(
    private val dataStoreManager: DataStoreManager
) : SettingsRepository {

    override val fareSettingsFlow: Flow<FareSettings> = combine(
        dataStoreManager.getInteger(KEY_CITY_ADULT_PRICE, FareSettings.DEFAULT_CITY_ADULT_PRICE),
        dataStoreManager.getInteger(KEY_CITY_CHILD_PRICE, FareSettings.DEFAULT_CITY_CHILD_PRICE),
        dataStoreManager.getInteger(KEY_VILLAGE_ADULT_PRICE, FareSettings.DEFAULT_VILLAGE_ADULT_PRICE),
        dataStoreManager.getInteger(KEY_VILLAGE_CHILD_PRICE, FareSettings.DEFAULT_VILLAGE_CHILD_PRICE),
        dataStoreManager.getString(KEY_PRESET_AMOUNTS, FareSettings.DEFAULT_PRESETS.joinToString(","))
    ) { cityAdult, cityChild, villageAdult, villageChild, presetsStr ->
        val parsedPresets = parsePresets(presetsStr)
        FareSettings(
            cityAdultPrice = cityAdult,
            cityChildPrice = cityChild,
            villageAdultPrice = villageAdult,
            villageChildPrice = villageChild,
            presetAmounts = parsedPresets
        )
    }

    override suspend fun saveFareSettings(settings: FareSettings) {
        val sortedPresets = settings.presetAmounts.sorted()
        dataStoreManager.edit {
            setInteger(KEY_CITY_ADULT_PRICE, settings.cityAdultPrice)
            setInteger(KEY_CITY_CHILD_PRICE, settings.cityChildPrice)
            setInteger(KEY_VILLAGE_ADULT_PRICE, settings.villageAdultPrice)
            setInteger(KEY_VILLAGE_CHILD_PRICE, settings.villageChildPrice)
            setString(KEY_PRESET_AMOUNTS, sortedPresets.joinToString(","))
        }
    }

    override suspend fun resetToDefaults() {
        dataStoreManager.clear()
    }

    private fun parsePresets(presetsStr: String): List<Int> {
        val parsed = presetsStr.split(",")
            .mapNotNull { it.trim().toIntOrNull() }
            .take(16)
            .sorted()

        return if (parsed.size == 16) {
            parsed
        } else {
            FareSettings.DEFAULT_PRESETS.sorted()
        }
    }
}
