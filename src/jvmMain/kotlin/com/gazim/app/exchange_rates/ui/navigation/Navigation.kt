package com.gazim.app.exchange_rates.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.gazim.app.exchange_rates.ui.screen.exchange_screen.ExchangeScreen
import com.gazim.app.exchange_rates.ui.screen.exchange_screen.ExchangeScreenViewModel
import com.gazim.app.exchange_rates.ui.screen.home_screen.HomeScreen
import com.gazim.app.exchange_rates.ui.screen.home_screen.HomeViewModel

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val currentRoute = remember {
        mutableStateListOf<Route>().apply {
            add(HomeScreenRoute)
        }
    }
    currentRoute.forEach {
        when (it) {
            is HomeScreenRoute -> Box(Modifier.fillMaxSize()) {
                Box(Modifier.fillMaxSize(fraction = 1f)) {
                    HomeScreen(remember { HomeViewModel() }) { item ->
                        currentRoute.add(
                            ExchangeScreenRoute(item)
                        )
                    }
                }

            }

            is ExchangeScreenRoute -> {
                ExchangeScreen(remember { ExchangeScreenViewModel(it.exchange) }) {
                    currentRoute.remove(it)
                }
            }
        }
    }
}