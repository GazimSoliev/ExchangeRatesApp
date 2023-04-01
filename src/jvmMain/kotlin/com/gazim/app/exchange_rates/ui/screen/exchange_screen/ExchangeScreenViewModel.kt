package com.gazim.app.exchange_rates.ui.screen.exchange_screen

import com.gazim.app.exchange_rates.ui.model.ExchangeScreenState
import com.gazim.app.exchange_rates.ui.model.NetworkResultStatus
import com.gazim.library.exchange_rates.model.IExchange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow

class ExchangeScreenViewModel(val exchange: IExchange) {
    val coroutineScope = CoroutineScope(Dispatchers.IO)
    val exchangeScreenState = MutableStateFlow(ExchangeScreenState(NetworkResultStatus.Loading, emptyList()))
}