package com.gazim.app.exchange_rates.ui.model

import com.gazim.app.exchange_rates.ui.screen.home_screen.ValuteFlagAlternative

data class ValutePresentation(
    val id: Int,
    val nominal: String,
    val value: String,
    val charCode: String,
    val name: String,
    val numCode: String,
    val changingLastFiveDay: List<DateValue>,
    val country: ValuteFlagAlternative?
)