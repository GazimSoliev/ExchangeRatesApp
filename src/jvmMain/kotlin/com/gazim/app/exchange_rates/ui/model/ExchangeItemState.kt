package com.gazim.app.exchange_rates.ui.model

import com.gazim.library.exchange_rates.model.IExchange

data class ExchangeItemState(
    val exchange: IExchange,
    val id: Int,
    val nominal: String,
    val value: String,
    val charCode: String,
    val name: String,
    val numCode: String,
    val changingLastFiveDay: List<DateValue>,
    val country: ExchangeFlag?
)