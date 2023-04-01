package com.gazim.app.exchange_rates.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import com.gazim.app.exchange_rates.ui.model.ExchangeScreenState
import com.gazim.app.exchange_rates.ui.screen.exchange_screen.GraphViewModel
import kotlinx.coroutines.flow.map

@Composable
fun ExchangeScreenComponent(state: ExchangeScreenState, onBack: () -> Unit) {
    val graphViewModel = remember(state.points) { GraphViewModel(state.points) }
    val offsets by graphViewModel.points.map { list ->
        list.map {
            Offset(it.x, it.y)
        }
    }.collectAsState(emptyList())
    Box {
        Canvas(Modifier.fillMaxSize()) {
            graphViewModel.get(size.height, size.width)
            drawPoints(points = offsets, PointMode.Polygon, color = Color.Red, strokeWidth = 1f)
        }
        FilledTonalIconButton(modifier = Modifier.align(Alignment.TopStart), onClick = onBack) {
            Icon(Icons.Default.ArrowBack, "Back to Home Screen")
        }
    }
}