package com.minibusfare.calculator.ui.fare.mvi

sealed interface FareCalculatorEvent {
    data class ShowToast(val message: String) : FareCalculatorEvent
}
