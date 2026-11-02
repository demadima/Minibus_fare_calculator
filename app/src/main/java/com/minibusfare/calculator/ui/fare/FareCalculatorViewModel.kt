package com.minibusfare.calculator.ui.fare

import androidx.lifecycle.ViewModel
import com.minibusfare.calculator.domain.model.RouteType
import com.minibusfare.calculator.ui.fare.mvi.FareCalculatorAction
import com.minibusfare.calculator.ui.fare.mvi.FareCalculatorEvent
import com.minibusfare.calculator.ui.fare.mvi.FareCalculatorState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class FareCalculatorViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        createCalculatedState(
            selectedRoute = RouteType.CITY,
            adultCount = 1,
            childCount = 0,
            amountPaidInput = ""
        )
    )
    val state: StateFlow<FareCalculatorState> = _state.asStateFlow()

    private val _event = Channel<FareCalculatorEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    fun onAction(action: FareCalculatorAction) {
        when (action) {
            is FareCalculatorAction.OnRouteSelected -> {
                updateState { it.copy(selectedRoute = action.route) }
            }
            is FareCalculatorAction.OnAdultCountChanged -> {
                updateState { it.copy(adultCount = action.count.coerceIn(0, 5)) }
            }
            is FareCalculatorAction.OnChildCountChanged -> {
                updateState { it.copy(childCount = action.count.coerceIn(0, 5)) }
            }
            is FareCalculatorAction.OnAmountPaidChanged -> {
                val digitsOnly = action.amount.filter { it.isDigit() }
                updateState { it.copy(amountPaidInput = digitsOnly) }
            }
            is FareCalculatorAction.OnPresetSelected -> {
                updateState { it.copy(amountPaidInput = action.amount.toString()) }
            }
            is FareCalculatorAction.OnClearPaidClicked -> {
                updateState { it.copy(amountPaidInput = "") }
            }
            is FareCalculatorAction.OnResetAllClicked -> {
                _state.value = createCalculatedState(
                    selectedRoute = _state.value.selectedRoute,
                    adultCount = 1,
                    childCount = 0,
                    amountPaidInput = ""
                )
            }
        }
    }

    private fun updateState(transform: (FareCalculatorState) -> FareCalculatorState) {
        _state.update { current ->
            val updated = transform(current)
            createCalculatedState(
                selectedRoute = updated.selectedRoute,
                adultCount = updated.adultCount,
                childCount = updated.childCount,
                amountPaidInput = updated.amountPaidInput
            )
        }
    }

    private fun createCalculatedState(
        selectedRoute: RouteType,
        adultCount: Int,
        childCount: Int,
        amountPaidInput: String
    ): FareCalculatorState {
        val totalFare = (adultCount * selectedRoute.adultPrice) + (childCount * selectedRoute.childPrice)
        val amountPaid = amountPaidInput.toIntOrNull() ?: 0
        val change = amountPaid - totalFare
        val isPaidSufficient = amountPaidInput.isNotEmpty() && (amountPaid >= totalFare)
        val remainingToPay = if (totalFare > amountPaid) totalFare - amountPaid else 0
        val breakdown = if (change > 0) calculateDenominations(change) else emptyMap()

        return FareCalculatorState(
            selectedRoute = selectedRoute,
            adultCount = adultCount,
            childCount = childCount,
            amountPaidInput = amountPaidInput,
            totalFare = totalFare,
            amountPaid = amountPaid,
            change = change,
            isPaidSufficient = isPaidSufficient,
            remainingToPay = remainingToPay,
            changeBreakdown = breakdown
        )
    }

    private fun calculateDenominations(amount: Int): Map<Int, Int> {
        val result = mutableMapOf<Int, Int>()
        var rem = amount
        val denominations = listOf(1000, 500, 200, 100, 50, 20, 10, 5, 2, 1)
        for (denom in denominations) {
            val count = rem / denom
            if (count > 0) {
                result[denom] = count
                rem %= denom
            }
        }
        return result
    }
}
