package com.minibusfare.calculator.ui.fare.mvi

import com.minibusfare.calculator.domain.model.RouteType

data class FareCalculatorState(
    val selectedRoute: RouteType = RouteType.CITY,
    val adultCount: Int = 1,
    val childCount: Int = 0,
    val amountPaidInput: String = "",
    val totalFare: Int = 15,
    val amountPaid: Int = 0,
    val change: Int = -15,
    val isPaidSufficient: Boolean = false,
    val remainingToPay: Int = 15,
    val changeBreakdown: Map<Int, Int> = emptyMap(),
    val presetAmounts: List<Int> = listOf(40, 50, 60, 70, 80, 90, 100, 120, 140, 150, 160, 200, 500, 1000, 2000)
)
