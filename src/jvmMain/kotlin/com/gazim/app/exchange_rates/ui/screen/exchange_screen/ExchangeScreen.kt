package com.gazim.app.exchange_rates.ui.screen.exchange_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ExchangeScreen(exchangeScreenViewModel: ExchangeScreenViewModel, onBackButton: () -> Unit) {
    Box(Modifier) {
        FilledIconButton(modifier = Modifier.align(Alignment.TopStart), onClick = {onBackButton()}) {
            Icon(Icons.Default.ArrowBack, "Back to Home Screen")
        }
    }
}

@Composable
fun ExchangeScreenComponent() {

}