package com.minibusfare.calculator

import com.minibusfare.calculator.domain.model.FareSettings
import com.minibusfare.calculator.domain.model.RouteType
import com.minibusfare.calculator.domain.repository.SettingsRepository
import com.minibusfare.calculator.ui.fare.FareCalculatorViewModel
import com.minibusfare.calculator.ui.fare.mvi.FareCalculatorAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FakeSettingsRepository : SettingsRepository {
    val settingsFlow = MutableStateFlow(FareSettings())
    override val fareSettingsFlow: Flow<FareSettings> = settingsFlow
    override suspend fun saveFareSettings(settings: FareSettings) {
        settingsFlow.value = settings
    }
    override suspend fun resetToDefaults() {
        settingsFlow.value = FareSettings()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class FareCalculatorViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeSettingsRepository
    private lateinit var viewModel: FareCalculatorViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeSettingsRepository()
        viewModel = FareCalculatorViewModel(fakeRepository)
        testDispatcher.scheduler.advanceUntilIdle()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `default initial state is City route with 1 adult and 0 children`() {
        val state = viewModel.state.value
        assertEquals(RouteType.CITY, state.selectedRoute)
        assertEquals(1, state.adultCount)
        assertEquals(0, state.childCount)
        assertEquals(15, state.totalFare)
        assertEquals("", state.amountPaidInput)
        assertEquals(0, state.amountPaid)
    }

    @Test
    fun `selecting Village route updates fares correctly`() {
        viewModel.onAction(FareCalculatorAction.OnRouteSelected(RouteType.VILLAGE))
        val state = viewModel.state.value
        assertEquals(RouteType.VILLAGE, state.selectedRoute)
        // 1 adult = 28
        assertEquals(28, state.totalFare)
    }

    @Test
    fun `calculates fare for multiple adults and children`() {
        // Village: adult=28, child=15
        viewModel.onAction(FareCalculatorAction.OnRouteSelected(RouteType.VILLAGE))
        viewModel.onAction(FareCalculatorAction.OnAdultCountChanged(2)) // 2 * 28 = 56
        viewModel.onAction(FareCalculatorAction.OnChildCountChanged(3)) // 3 * 15 = 45
        // Total = 101

        val state = viewModel.state.value
        assertEquals(101, state.totalFare)
    }

    @Test
    fun `preset selection updates amount paid and calculates change`() {
        // City: 1 adult = 15
        viewModel.onAction(FareCalculatorAction.OnPresetSelected(50))

        val state = viewModel.state.value
        assertEquals("50", state.amountPaidInput)
        assertEquals(50, state.amountPaid)
        assertEquals(35, state.change) // 50 - 15 = 35
        assertTrue(state.isPaidSufficient)
    }

    @Test
    fun `insufficient amount paid shows correct remaining amount`() {
        // City: 1 adult = 15
        viewModel.onAction(FareCalculatorAction.OnAmountPaidChanged("10"))

        val state = viewModel.state.value
        assertEquals(10, state.amountPaid)
        assertEquals(-5, state.change)
        assertEquals(5, state.remainingToPay)
        assertFalse(state.isPaidSufficient)
    }

    @Test
    fun `breakdown of change calculates banknote denominations`() {
        viewModel.onAction(FareCalculatorAction.OnRouteSelected(RouteType.CITY)) // 15
        viewModel.onAction(FareCalculatorAction.OnPresetSelected(100)) // paid 100 -> change = 85

        val state = viewModel.state.value
        assertEquals(85, state.change)
        // 85 = 50x1 + 20x1 + 10x1 + 5x1
        val breakdown = state.changeBreakdown
        assertEquals(1, breakdown[50])
        assertEquals(1, breakdown[20])
        assertEquals(1, breakdown[10])
        assertEquals(1, breakdown[5])
    }

    @Test
    fun `reset action restores default state`() {
        viewModel.onAction(FareCalculatorAction.OnRouteSelected(RouteType.VILLAGE))
        viewModel.onAction(FareCalculatorAction.OnAdultCountChanged(4))
        viewModel.onAction(FareCalculatorAction.OnPresetSelected(100))

        viewModel.onAction(FareCalculatorAction.OnResetAllClicked)

        val state = viewModel.state.value
        assertEquals(1, state.adultCount)
        assertEquals(0, state.childCount)
        assertEquals("", state.amountPaidInput)
    }
}
