package com.gazim.app.exchange_rates.ui.model

import com.gazim.app.exchange_rates.math.Point

data class GraphData(
    val list: List<Point>,
    val points: List<GraphPoints>,
    val lines: List<Float>,
    val max: Float,
    val min: Float
)