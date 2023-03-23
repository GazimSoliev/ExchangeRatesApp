package com.gazim.app.exchange_rates.ui.screen.home_screen

import com.gazim.app.exchange_rates.ui.model.DateValue
import com.gazim.app.exchange_rates.ui.model.ExchangePresentation
import com.gazim.library.exchange_rates.model.ExchangeRatesDataProperty
import com.gazim.library.exchange_rates.model.IVarCus
import com.gazim.library.exchange_rates.repository.ExchangeRatesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel {
    val viewModelScope = CoroutineScope(Job() + Dispatchers.IO)
    val list = MutableStateFlow(emptyList<ExchangePresentation>())
    val data = MutableStateFlow(LocalDate.now())

    fun getList(localDate: LocalDate) = viewModelScope.launch(Dispatchers.IO) {
        Thread.currentThread().name
//        data.value = with(localDate) { "${numFormat(dayOfMonth)}.${numFormat(monthValue)}.$year" }
        val lastFiveDay = LinkedHashSet<IVarCus>()
        var i = 0
        var count = 0
        while (i < 5 && count < 16) {
            val varCus =
                ExchangeRatesRepository.getExchangeRates(ExchangeRatesDataProperty(localDate.minusDays(count.toLong())))
            if (lastFiveDay.add(varCus)) i++
            count++
        }
        list.value = lastFiveDay.first().also {
            data.value = it.date
        }.exchanges.mapIndexed { index, it ->
            with(it) {
                ExchangePresentation(id = index,
                    nominal = nominal.toString(),
                    value = "$value ₽",
                    charCode = charCode,
                    name = name,
                    numCode = numCode,
                    changingLastFiveDay = lastFiveDay.map { c ->
                        DateValue(
                            date = c.date.run { "${numFormat(dayOfMonth)}.${numFormat(monthValue)}" },
                            value = c.exchanges[index].value
                        )
                    }.reversed(),
                    country = ExchangeFlagAlternative.values().find { it.exchange == charCode }
                )
            }
        }
    }

    fun numFormat(num: Int): String {
        val numStr = num.toString()
        return when (numStr.length) {
            1 -> "0$numStr"
            2 -> numStr
            else -> numStr.substring(numStr.length - 2)
        }
    }
}