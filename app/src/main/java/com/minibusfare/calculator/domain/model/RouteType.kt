package com.minibusfare.calculator.domain.model

enum class RouteType(
    val title: String,
    val adultPrice: Int,
    val childPrice: Int
) {
    CITY("По місту", adultPrice = 15, childPrice = 8),
    VILLAGE("До села", adultPrice = 28, childPrice = 15)
}
