package com.gazim.app.exchange_rates.ui.model

import java.time.LocalDate

data class HomeScreenState(
    val date: LocalDate,
    val strDate: String,
    val nameVarCus: String,
    val isLoaded: NetworkResultStatus,
    val errorMsg: String = "No internet",
    val exchanges: List<ExchangePresentation>
)

