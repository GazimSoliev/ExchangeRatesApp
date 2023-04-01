package com.gazim.app.exchange_rates.ui.model

import com.gazim.library.exchange_rates.model.IRecord

data class ExchangeScreenState(
    val networkResultStatus: NetworkResultStatus,
    val points: List<IRecord>
)
