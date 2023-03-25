package com.gazim.app.exchange_rates.ui.model

data class ExchangePresentation(
    val id: Int,
    val nominal: String,
    val value: String,
    val charCode: String,
    val name: String,
    val numCode: String,
    val changingLastFiveDay: List<DateValue>,
    val country: ExchangeFlag?
)