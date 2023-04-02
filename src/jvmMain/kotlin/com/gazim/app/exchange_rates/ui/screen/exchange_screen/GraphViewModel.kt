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
    var sHeight = 0f
    var sWidth = 0f

    fun get(height: Float, width: Float) =
        viewModel.launch(Dispatchers.Default) {
            if (records.size < 2) {
                points.value = emptyList()
                return@launch
            }
            println("Test1324")
            if (sHeight == height && sWidth == width) return@launch
            println("Passed")
            sHeight = height
            sWidth = width
            var currentWidth = 0f
            val exchangeValues = records.map { it.value / it.nominal }
            val max = exchangeValues.max()
            val min = exchangeValues.min()
            val step = width / (records.size - 1)
            val offsetX = max - min
            points.value = exchangeValues.map {
                currentWidth += step
                Point(
                    x = currentWidth,
                    y = height - (it - min) / offsetX * height
                )
            }
        }

}