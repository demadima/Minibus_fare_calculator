package com.minibusfare.calculator.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.minibusfare.calculator.ui.fare.FareCalculatorScreen
import com.minibusfare.calculator.ui.settings.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = CalculatorRoute,
        modifier = modifier,
        enterTransition = {
            slideInHorizontally(animationSpec = tween(400)) { it } + fadeIn(animationSpec = tween(400))
        },
        exitTransition = {
            slideOutHorizontally(animationSpec = tween(400)) { -it } + fadeOut(animationSpec = tween(400))
        },
        popEnterTransition = {
            slideInHorizontally(animationSpec = tween(400)) { -it } + fadeIn(animationSpec = tween(400))
        },
        popExitTransition = {
            slideOutHorizontally(animationSpec = tween(400)) { it } + fadeOut(animationSpec = tween(400))
        }
    ) {
        composable<CalculatorRoute> {
            FareCalculatorScreen(
                onNavigateToSettings = {
                    navController.navigate(SettingsRoute)
                }
            )
        }

        composable<SettingsRoute> {
            SettingsScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
