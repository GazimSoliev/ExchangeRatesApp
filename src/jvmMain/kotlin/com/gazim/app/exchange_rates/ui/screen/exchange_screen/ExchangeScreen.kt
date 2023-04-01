package com.gazim.app.exchange_rates.ui.screen.exchange_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gazim.app.exchange_rates.ui.components.ExchangeScreenComponent

class ExchangeScreen(private val exchangeScreenViewModel: ExchangeScreenViewModel) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        rememberSaveable(this) { exchangeScreenViewModel.get() }
        val exchangeScreenState by exchangeScreenViewModel.exchangeScreenState.collectAsState()
        ExchangeScreenComponent(exchangeScreenState) {
            navigator.pop()
        }
    }

}
