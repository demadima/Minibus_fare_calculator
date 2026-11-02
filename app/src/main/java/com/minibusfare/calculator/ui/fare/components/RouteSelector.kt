package com.minibusfare.calculator.ui.fare.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minibusfare.calculator.R
import com.minibusfare.calculator.domain.model.RouteType
import com.minibusfare.calculator.ui.theme.CityBlueContainerDark
import com.minibusfare.calculator.ui.theme.CityBlueContainerLight
import com.minibusfare.calculator.ui.theme.CityBlueOnContainerDark
import com.minibusfare.calculator.ui.theme.CityBlueOnContainerLight
import com.minibusfare.calculator.ui.theme.CityBluePrimaryDark
import com.minibusfare.calculator.ui.theme.CityBluePrimaryLight
import com.minibusfare.calculator.ui.theme.MinibusFareCalculatorTheme
import com.minibusfare.calculator.ui.theme.VillageOrangeContainerDark
import com.minibusfare.calculator.ui.theme.VillageOrangeContainerLight
import com.minibusfare.calculator.ui.theme.VillageOrangeOnContainerDark
import com.minibusfare.calculator.ui.theme.VillageOrangeOnContainerLight
import com.minibusfare.calculator.ui.theme.VillageOrangePrimaryDark
import com.minibusfare.calculator.ui.theme.VillageOrangePrimaryLight

@Composable
fun RouteSelector(
    selectedRoute: RouteType,
    onRouteSelected: (RouteType) -> Unit,
    modifier: Modifier = Modifier,
    cardVerticalPadding: Dp = 6.dp
) {
    val darkTheme = isSystemInDarkTheme()

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RouteType.entries.forEach { route ->
            val isSelected = route == selectedRoute

            val routePrimary = when (route) {
                RouteType.CITY -> if (darkTheme) CityBluePrimaryDark else CityBluePrimaryLight
                RouteType.VILLAGE -> if (darkTheme) VillageOrangePrimaryDark else VillageOrangePrimaryLight
            }
            val routeContainer = when (route) {
                RouteType.CITY -> if (darkTheme) CityBlueContainerDark else CityBlueContainerLight
                RouteType.VILLAGE -> if (darkTheme) VillageOrangeContainerDark else VillageOrangeContainerLight
            }
            val routeOnContainer = when (route) {
                RouteType.CITY -> if (darkTheme) CityBlueOnContainerDark else CityBlueOnContainerLight
                RouteType.VILLAGE -> if (darkTheme) VillageOrangeOnContainerDark else VillageOrangeOnContainerLight
            }

            val backgroundColor = if (isSelected) routeContainer else MaterialTheme.colorScheme.surface
            val contentColor = if (isSelected) routeOnContainer else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
            val borderColor = if (isSelected) routePrimary else routePrimary.copy(alpha = 0.45f)

            Surface(
                selected = isSelected,
                onClick = { onRouteSelected(route) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                color = backgroundColor,
                contentColor = contentColor,
                border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = cardVerticalPadding, horizontal = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = route.title,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                        fontSize = 15.sp,
                        color = if (isSelected) contentColor else routePrimary
                    )
                    Text(
                        text = stringResource(R.string.route_price_format, route.adultPrice, route.childPrice),
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.sp,
                        color = contentColor.copy(alpha = 0.85f)
                    )
                }
            }
        }
    }
}

@Preview(name = "Route Selector City Selected", showBackground = true)
@Composable
fun RouteSelectorCityPreview() {
    MinibusFareCalculatorTheme(routeType = RouteType.CITY) {
        RouteSelector(
            selectedRoute = RouteType.CITY,
            onRouteSelected = {},
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Preview(name = "Route Selector Village Selected", showBackground = true)
@Composable
fun RouteSelectorVillagePreview() {
    MinibusFareCalculatorTheme(routeType = RouteType.VILLAGE) {
        RouteSelector(
            selectedRoute = RouteType.VILLAGE,
            onRouteSelected = {},
            modifier = Modifier.padding(12.dp)
        )
    }
}
