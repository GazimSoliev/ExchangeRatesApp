package com.gazim.app.exchange_rates.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
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
    var pointPositionX by remember { mutableStateOf(-100f) }
    var pointPositionY by remember { mutableStateOf(0f) }
    LaunchedEffect(pointPositionX) {
        offsets.size.let {
            if (it > 1)
                repeat(it - 1) { i ->
                    if (pointPositionX in offsets[i].x..offsets[i + 1].x) {
                        pointPositionY = offsets[i].y
                        return@let
                    }
                }
        }
    }
    var pointPosition by remember { mutableStateOf(Offset(0f, 0f)) }
    Box {
        Canvas(Modifier.fillMaxSize().padding(32.dp).pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    val position = event.changes.first().position
                    // on every relayout Compose will send synthetic Move event,
                    // so we skip it to avoid event spam
                    println("EventMouseType: ${event.type}")
                    if (event.type == PointerEventType.Move) {
                        pointPositionX = position.x
                    } else if (event.type == PointerEventType.Exit) {
                        pointPositionX = -100f
                    }
                }
            }
        }) {
            graphViewModel.get(size.height, size.width)
            drawPoints(points = offsets, PointMode.Polygon, color = Color.Red, strokeWidth = 1f)
            println("test")
            drawLine(Color.Gray, start = Offset(pointPositionX, 0f), end = Offset(pointPositionX, size.height))
            drawCircle(Color.Green, radius = 5f, Offset(pointPositionX, pointPositionY))
        }
        FilledTonalIconButton(modifier = Modifier.align(Alignment.TopStart), onClick = onBack) {
            Icon(Icons.Default.ArrowBack, "Back to Home Screen")
        }
    }
}