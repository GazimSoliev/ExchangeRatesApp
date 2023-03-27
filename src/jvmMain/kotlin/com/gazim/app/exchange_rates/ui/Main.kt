package com.gazim.app.exchange_rates.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.gazim.app.exchange_rates.ui.screen.home_screen.HomeScreen
import com.gazim.app.exchange_rates.ui.screen.home_screen.HomeViewModel
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
            HomeScreen(remember { HomeViewModel() })
        }
    }
}