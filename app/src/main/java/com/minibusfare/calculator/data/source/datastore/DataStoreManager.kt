package com.minibusfare.calculator.data.source.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.minibusfare.calculator.data.source.datastore.ds_const.DATASTORE_FILE_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath

fun createDataStore(context: Context): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { context.filesDir.resolve(DATASTORE_FILE_NAME).absolutePath.toPath() }
    )

/**
 * DataStoreManager — обгортка над Jetpack DataStore для збереження налаштувань (цін та купюр).
 */
class DataStoreManager(private val dataStore: DataStore<Preferences>) {

    fun getInteger(key: String, default: Int): Flow<Int> =
        dataStore.data.map { it[intPreferencesKey(key)] ?: default }

    fun getString(key: String, default: String): Flow<String> =
        dataStore.data.map { it[stringPreferencesKey(key)] ?: default }

    suspend fun setInteger(key: String, value: Int) = edit { setInteger(key, value) }
    suspend fun setString(key: String, value: String) = edit { setString(key, value) }

    suspend fun edit(block: suspend PreferencesEditor.() -> Unit) {
        dataStore.edit { preferences ->
            PreferencesEditor(preferences).apply { block() }
        }
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    class PreferencesEditor(private val preferences: MutablePreferences) {
        fun setInteger(key: String, value: Int) {
            preferences[intPreferencesKey(key)] = value
        }

        fun setString(key: String, value: String) {
            preferences[stringPreferencesKey(key)] = value
        }

        fun remove(key: String) {
            preferences.remove(intPreferencesKey(key))
            preferences.remove(stringPreferencesKey(key))
        }
    }
}
