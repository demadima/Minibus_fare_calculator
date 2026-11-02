package com.minibusfare.calculator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.minibusfare.calculator.domain.model.RouteType

private val CityLightColorScheme = lightColorScheme(
    primary = CityBluePrimaryLight,
    onPrimary = CityBlueOnPrimaryLight,
    primaryContainer = CityBlueContainerLight,
    onPrimaryContainer = CityBlueOnContainerLight,
    surfaceVariant = CityBlueSurfaceVariantLight
)

private val CityDarkColorScheme = darkColorScheme(
    primary = CityBluePrimaryDark,
    onPrimary = CityBlueOnPrimaryDark,
    primaryContainer = CityBlueContainerDark,
    onPrimaryContainer = CityBlueOnContainerDark,
    surfaceVariant = CityBlueSurfaceVariantDark
)

private val VillageLightColorScheme = lightColorScheme(
    primary = VillageOrangePrimaryLight,
    onPrimary = VillageOrangeOnPrimaryLight,
    primaryContainer = VillageOrangeContainerLight,
    onPrimaryContainer = VillageOrangeOnContainerLight,
    surfaceVariant = VillageOrangeSurfaceVariantLight
)

private val VillageDarkColorScheme = darkColorScheme(
    primary = VillageOrangePrimaryDark,
    onPrimary = VillageOrangeOnPrimaryDark,
    primaryContainer = VillageOrangeContainerDark,
    onPrimaryContainer = VillageOrangeOnContainerDark,
    surfaceVariant = VillageOrangeSurfaceVariantDark
)

@Composable
fun MinibusFareCalculatorTheme(
    routeType: RouteType = RouteType.CITY,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when (routeType) {
        RouteType.CITY -> if (darkTheme) CityDarkColorScheme else CityLightColorScheme
        RouteType.VILLAGE -> if (darkTheme) VillageDarkColorScheme else VillageLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
