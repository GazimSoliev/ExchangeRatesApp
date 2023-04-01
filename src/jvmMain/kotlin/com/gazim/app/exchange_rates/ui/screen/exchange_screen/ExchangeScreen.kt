package com.gazim.app.exchange_rates.ui.screen.exchange_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gazim.app.exchange_rates.ui.components.ExchangeScreenComponent

class ExchangeScreen(private val exchangeScreenViewModel: ExchangeScreenViewModel) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        ExchangeScreenComponent {
            navigator.pop()
        }
    }

}
