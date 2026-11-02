package com.minibusfare.calculator.ui.fare.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minibusfare.calculator.R
import com.minibusfare.calculator.domain.model.RouteType
import com.minibusfare.calculator.ui.fare.mvi.FareCalculatorState
import com.minibusfare.calculator.ui.theme.MinibusFareCalculatorTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChangeSummaryCard(
    state: FareCalculatorState,
    onResetClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Fare Summary Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.label_to_pay),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "${state.totalFare} ₴",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (state.amountPaidInput.isNotEmpty()) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = stringResource(R.string.label_received),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "${state.amountPaid} ₴",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Change / Status Box
            val isPaid = state.amountPaidInput.isNotEmpty()
            val change = state.change

            val containerColor: Color
            val contentColor: Color
            val statusTitle: String
            val statusValue: String

            when {
                !isPaid -> {
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    statusTitle = stringResource(R.string.title_enter_amount)
                    statusValue = stringResource(R.string.subtitle_enter_amount)
                }
                change > 0 -> {
                    containerColor = Color(0xFF2E7D32) // Green 800
                    contentColor = Color.White
                    statusTitle = stringResource(R.string.title_change)
                    statusValue = "$change ₴"
                }
                change == 0 -> {
                    containerColor = MaterialTheme.colorScheme.primary
                    contentColor = MaterialTheme.colorScheme.onPrimary
                    statusTitle = stringResource(R.string.title_no_change)
                    statusValue = stringResource(R.string.value_no_change_format, state.totalFare)
                }
                else -> {
                    containerColor = Color(0xFFD32F2F) // Red 700
                    contentColor = Color.White
                    statusTitle = stringResource(R.string.title_insufficient)
                    statusValue = "${state.remainingToPay} ₴"
                }
            }

            val statusBoxModifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isPaid) {
                        Modifier.clickable { onResetClicked() }
                    } else Modifier
                )

            Card(
                modifier = statusBoxModifier,
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = containerColor,
                    contentColor = contentColor
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = statusTitle,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = contentColor.copy(alpha = 0.9f)
                    )
                    Text(
                        text = statusValue,
                        fontSize = if (change > 0) 24.sp else 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = contentColor
                    )
                    if (isPaid) {
                        Text(
                            text = stringResource(R.string.hint_tap_to_reset),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Normal,
                            color = contentColor.copy(alpha = 0.8f),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }

            // Denomination Breakdown Chips
            if (change > 0 && state.changeBreakdown.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Text(
                        text = stringResource(R.string.label_denominations),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        state.changeBreakdown.forEach { (denom, count) ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
                            ) {
                                Text(
                                    text = stringResource(R.string.denomination_item_format, denom, count),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "1. Waiting for Input", showBackground = true)
@Composable
fun ChangeSummaryCardWaitingPreview() {
    MinibusFareCalculatorTheme(routeType = RouteType.CITY) {
        ChangeSummaryCard(
            state = FareCalculatorState(
                selectedRoute = RouteType.CITY,
                adultCount = 1,
                totalFare = 15,
                amountPaidInput = "",
                amountPaid = 0,
                change = -15
            ),
            onResetClicked = {},
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Preview(name = "2. Change with Denominations", showBackground = true)
@Composable
fun ChangeSummaryCardWithChangePreview() {
    MinibusFareCalculatorTheme(routeType = RouteType.CITY) {
        ChangeSummaryCard(
            state = FareCalculatorState(
                selectedRoute = RouteType.CITY,
                adultCount = 1,
                totalFare = 15,
                amountPaidInput = "100",
                amountPaid = 100,
                change = 85,
                isPaidSufficient = true,
                remainingToPay = 0,
                changeBreakdown = mapOf(50 to 1, 20 to 1, 10 to 1, 5 to 1)
            ),
            onResetClicked = {},
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Preview(name = "3. Exact Paid", showBackground = true)
@Composable
fun ChangeSummaryCardExactPreview() {
    MinibusFareCalculatorTheme(routeType = RouteType.VILLAGE) {
        ChangeSummaryCard(
            state = FareCalculatorState(
                selectedRoute = RouteType.VILLAGE,
                adultCount = 1,
                totalFare = 28,
                amountPaidInput = "28",
                amountPaid = 28,
                change = 0,
                isPaidSufficient = true,
                remainingToPay = 0
            ),
            onResetClicked = {},
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Preview(name = "4. Insufficient Amount", showBackground = true)
@Composable
fun ChangeSummaryCardInsufficientPreview() {
    MinibusFareCalculatorTheme(routeType = RouteType.CITY) {
        ChangeSummaryCard(
            state = FareCalculatorState(
                selectedRoute = RouteType.CITY,
                adultCount = 2,
                totalFare = 30,
                amountPaidInput = "20",
                amountPaid = 20,
                change = -10,
                isPaidSufficient = false,
                remainingToPay = 10
            ),
            onResetClicked = {},
            modifier = Modifier.padding(12.dp)
        )
    }
}
