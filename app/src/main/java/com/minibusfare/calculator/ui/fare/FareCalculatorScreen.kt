package com.minibusfare.calculator.ui.fare

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minibusfare.calculator.R
import com.minibusfare.calculator.domain.model.RouteType
import com.minibusfare.calculator.ui.fare.components.ChangeSummaryCard
import com.minibusfare.calculator.ui.fare.components.GivenAmountSection
import com.minibusfare.calculator.ui.fare.components.PassengerSelector
import com.minibusfare.calculator.ui.fare.components.PresetAmountGrid
import com.minibusfare.calculator.ui.fare.components.RouteSelector
import com.minibusfare.calculator.ui.fare.mvi.FareCalculatorAction
import com.minibusfare.calculator.ui.fare.mvi.FareCalculatorState
import com.minibusfare.calculator.ui.theme.MinibusFareCalculatorTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FareCalculatorScreen(
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FareCalculatorViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    FareCalculatorContent(
        state = state,
        onAction = viewModel::onAction,
        onNavigateToSettings = onNavigateToSettings,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FareCalculatorContent(
    state: FareCalculatorState,
    onAction: (FareCalculatorAction) -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    MinibusFareCalculatorTheme(routeType = state.selectedRoute) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.app_name),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    actions = {
                        IconButton(onClick = onNavigateToSettings) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = stringResource(R.string.title_settings)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            modifier = modifier
        ) { innerPadding ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .imePadding()
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                val screenHeight = maxHeight
                val isCompact = screenHeight < 680.dp

                val spacing = if (isCompact) 4.dp else 6.dp
                val buttonHeight = if (isCompact) 34.dp else 38.dp
                val fieldHeight = if (isCompact) 48.dp else 52.dp

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    // 1. Маршрут (По місту / До села)
                    RouteSelector(
                        selectedRoute = state.selectedRoute,
                        onRouteSelected = { onAction(FareCalculatorAction.OnRouteSelected(it)) },
                        cardVerticalPadding = if (isCompact) 4.dp else 6.dp
                    )

                    // 2. Кол-во пассажиров: Дорослі
                    PassengerSelector(
                        title = stringResource(R.string.label_adults),
                        pricePerPerson = state.getAdultPrice(),
                        selectedCount = state.adultCount,
                        onCountSelected = { onAction(FareCalculatorAction.OnAdultCountChanged(it)) },
                        maxCount = 5,
                        allowZero = true,
                        buttonHeight = buttonHeight
                    )

                    // 3. Кол-во пассажиров: Діти
                    PassengerSelector(
                        title = stringResource(R.string.label_children),
                        pricePerPerson = state.getChildPrice(),
                        selectedCount = state.childCount,
                        onCountSelected = { onAction(FareCalculatorAction.OnChildCountChanged(it)) },
                        maxCount = 5,
                        allowZero = true,
                        buttonHeight = buttonHeight
                    )

                    // 4. Шаблоны купюр (16 блоков)
                    Text(
                        text = stringResource(R.string.preset_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    PresetAmountGrid(
                        presets = state.presetAmounts,
                        currentAmountPaid = state.amountPaid,
                        onPresetSelected = { onAction(FareCalculatorAction.OnPresetSelected(it)) },
                        buttonHeight = buttonHeight
                    )

                    // 5. Ввод суммы от пассажира
                    GivenAmountSection(
                        amountPaidText = state.amountPaidInput,
                        onAmountPaidChanged = { onAction(FareCalculatorAction.OnAmountPaidChanged(it)) },
                        onClearPaidClicked = { onAction(FareCalculatorAction.OnClearPaidClicked) },
                        fieldHeight = fieldHeight
                    )

                    // 6. Итого и сдача (Клик по плашке сдачи/статуса выполняет сброс)
                    ChangeSummaryCard(
                        state = state,
                        onResetClicked = { onAction(FareCalculatorAction.OnResetAllClicked) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Preview(name = "City Screen (Blue Theme)", heightDp = 800, showBackground = true)
@Composable
fun FareCalculatorScreenCityPreview() {
    FareCalculatorContent(
        state = FareCalculatorState(
            selectedRoute = RouteType.CITY,
            adultCount = 1,
            childCount = 0,
            amountPaidInput = "100",
            totalFare = 15,
            amountPaid = 100,
            change = 85,
            isPaidSufficient = true,
            remainingToPay = 0,
            changeBreakdown = mapOf(50 to 1, 20 to 1, 10 to 1, 5 to 1)
        ),
        onAction = {},
        onNavigateToSettings = {}
    )
}

@Preview(name = "Village Screen (Orange Theme)", heightDp = 800, showBackground = true)
@Composable
fun FareCalculatorScreenVillagePreview() {
    FareCalculatorContent(
        state = FareCalculatorState(
            selectedRoute = RouteType.VILLAGE,
            adultCount = 1,
            childCount = 0,
            amountPaidInput = "100",
            totalFare = 28,
            amountPaid = 100,
            change = 72,
            isPaidSufficient = true,
            remainingToPay = 0,
            changeBreakdown = mapOf(50 to 1, 20 to 1, 2 to 1)
        ),
        onAction = {},
        onNavigateToSettings = {}
    )
}
