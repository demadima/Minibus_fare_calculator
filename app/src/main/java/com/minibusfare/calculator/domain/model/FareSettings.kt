package com.minibusfare.calculator.domain.model

data class FareSettings(
    val cityAdultPrice: Int = DEFAULT_CITY_ADULT_PRICE,
    val cityChildPrice: Int = DEFAULT_CITY_CHILD_PRICE,
    val villageAdultPrice: Int = DEFAULT_VILLAGE_ADULT_PRICE,
    val villageChildPrice: Int = DEFAULT_VILLAGE_CHILD_PRICE,
    val presetAmounts: List<Int> = DEFAULT_PRESETS
) {
    fun getAdultPrice(route: RouteType): Int = when (route) {
        RouteType.CITY -> cityAdultPrice
        RouteType.VILLAGE -> villageAdultPrice
    }

    fun getChildPrice(route: RouteType): Int = when (route) {
        RouteType.CITY -> cityChildPrice
        RouteType.VILLAGE -> villageChildPrice
    }

    companion object {
        const val DEFAULT_CITY_ADULT_PRICE = 15
        const val DEFAULT_CITY_CHILD_PRICE = 8
        const val DEFAULT_VILLAGE_ADULT_PRICE = 28
        const val DEFAULT_VILLAGE_CHILD_PRICE = 15
        val DEFAULT_PRESETS = listOf(180, 40, 50, 60, 70, 80, 90, 100, 120, 140, 150, 160, 200, 500, 1000, 2000)
    }
}
