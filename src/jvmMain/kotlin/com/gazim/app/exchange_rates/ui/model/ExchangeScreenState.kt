package com.gazim.app.exchange_rates.ui.model

import com.gazim.app.exchange_rates.math.Point

data class ExchangeScreenState(
    val networkResultStatus: NetworkResultStatus,
    val points: List<Point>
)
