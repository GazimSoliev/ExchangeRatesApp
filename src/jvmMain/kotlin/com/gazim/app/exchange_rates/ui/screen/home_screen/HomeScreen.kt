package com.gazim.app.exchange_rates.ui.screen.home_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gazim.app.exchange_rates.ui.components.HomeScreenComponent
import com.gazim.app.exchange_rates.ui.screen.exchange_screen.ExchangeScreen
import com.gazim.app.exchange_rates.ui.screen.exchange_screen.ExchangeScreenViewModel
import java.time.LocalDate


class HomeScreen : Screen {
    val homeViewModel = HomeViewModel()

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        rememberSaveable(this) { homeViewModel.getList(LocalDate.now()) }
        val homeScreenState by homeViewModel.homeScreenState.collectAsState()
        HomeScreenComponent(
            homeScreenState,
            homeViewModel::getList,
            homeViewModel::update,
            homeScreenState.searchQuery,
            homeViewModel::setQuery
        ) { navigator.push(ExchangeScreen(ExchangeScreenViewModel(it))) }
    }

}

