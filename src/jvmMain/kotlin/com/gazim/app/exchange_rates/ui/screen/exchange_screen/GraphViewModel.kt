package com.gazim.app.exchange_rates.ui.screen.exchange_screen

import com.gazim.app.exchange_rates.math.Point
import com.gazim.library.exchange_rates.model.IRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GraphViewModel(val records: List<IRecord>) {
    val viewModel = CoroutineScope(Dispatchers.Default)
    val points = MutableStateFlow<List<Point>>(emptyList())

    fun get(height: Float, width: Float) =
        viewModel.launch(Dispatchers.Default) {
            var currentWidth = 0f
            val max = records.maxOf { it.value }
            val min = records.minOf { it.value }
            val step = width / (records.size - 1)
            val offsetX = max - min
            points.value = records.map {
                currentWidth += step
                Point(
                    x = currentWidth,
                    y = (it.value - min) / offsetX * height
                )
            }
        }

}