package com.minibusfare.calculator.di

import com.minibusfare.calculator.data.repository.SettingsRepositoryImpl
import com.minibusfare.calculator.data.source.datastore.DataStoreManager
import com.minibusfare.calculator.data.source.datastore.createDataStore
import com.minibusfare.calculator.domain.repository.SettingsRepository
import com.minibusfare.calculator.ui.fare.FareCalculatorViewModel
import com.minibusfare.calculator.ui.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { createDataStore(androidContext()) }
    singleOf(::DataStoreManager)
    singleOf(::SettingsRepositoryImpl) bind SettingsRepository::class

    viewModelOf(::FareCalculatorViewModel)
    viewModelOf(::SettingsViewModel)
}
