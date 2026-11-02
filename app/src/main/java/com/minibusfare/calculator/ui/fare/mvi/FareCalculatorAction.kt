package com.minibusfare.calculator.ui.fare.mvi

import com.minibusfare.calculator.domain.model.RouteType

sealed interface FareCalculatorAction {
    data class OnRouteSelected(val route: RouteType) : FareCalculatorAction
    data class OnAdultCountChanged(val count: Int) : FareCalculatorAction
    data class OnChildCountChanged(val count: Int) : FareCalculatorAction
    data class OnAmountPaidChanged(val amount: String) : FareCalculatorAction
    data class OnPresetSelected(val amount: Int) : FareCalculatorAction
    data object OnClearPaidClicked : FareCalculatorAction
    data object OnResetAllClicked : FareCalculatorAction
}
