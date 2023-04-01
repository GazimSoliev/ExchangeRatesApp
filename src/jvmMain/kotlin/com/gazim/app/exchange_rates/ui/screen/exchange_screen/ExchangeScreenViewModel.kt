package com.gazim.app.exchange_rates.ui.screen.exchange_screen

import com.gazim.app.exchange_rates.ui.model.ExchangeScreenState
import com.gazim.app.exchange_rates.ui.model.NetworkResultStatus
import com.gazim.library.exchange_rates.model.IExchange
import com.gazim.library.exchange_rates.model.record_er_property_models.DateRangeRecERProp
import com.gazim.library.exchange_rates.repository.RecordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class ExchangeScreenViewModel(val exchange: IExchange) {
    val coroutineScope = CoroutineScope(Dispatchers.IO)
    val exchangeScreenState = MutableStateFlow(ExchangeScreenState(NetworkResultStatus.Loading, emptyList()))
    fun get() = coroutineScope.launch {
        val localDate = LocalDate.now()
        val records = RecordRepository.getRecords(
            exchange,
            setOf(DateRangeRecERProp(startDate = localDate.minusYears(20), endDate = localDate))
        )
        exchangeScreenState.value =
            ExchangeScreenState(networkResultStatus = NetworkResultStatus.Success, points = records.records)
    }
}