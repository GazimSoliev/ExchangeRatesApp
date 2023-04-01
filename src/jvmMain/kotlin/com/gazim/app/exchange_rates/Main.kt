package com.gazim.app.exchange_rates

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cafe.adriel.voyager.navigator.Navigator
import com.gazim.app.exchange_rates.ui.screen.home_screen.HomeScreen
import com.gazim.app.exchange_rates.ui.theme.AppTheme

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Курс валют") {
        ExchangeRatesApp()
    }
}

@Composable
fun ExchangeRatesApp() {
    AppTheme {
        Surface(Modifier.fillMaxSize()) {
            Navigator(HomeScreen())
        }
    }
}