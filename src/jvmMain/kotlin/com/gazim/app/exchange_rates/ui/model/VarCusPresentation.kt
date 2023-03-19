package com.gazim.app.exchange_rates.ui.model

data class VarCusPresentation(
    val date: String,
    val name: String,
    val valutes: List<ValutePresentation>
)

