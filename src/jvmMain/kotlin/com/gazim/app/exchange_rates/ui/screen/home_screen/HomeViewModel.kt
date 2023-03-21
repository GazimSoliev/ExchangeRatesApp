package com.gazim.app.exchange_rates.ui.screen.home_screen

import com.gazim.app.exchange_rates.ui.model.DateValue
import com.gazim.app.exchange_rates.ui.model.ValutePresentation
import com.gazim.libray.exchange_rates.model.ExchangeRatesDataProperty
import com.gazim.libray.exchange_rates.model.IVarCus
import com.gazim.libray.exchange_rates.repository.ExchangeRatesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel {
    val viewModelScope = CoroutineScope(Job() + Dispatchers.IO)
    val list = MutableStateFlow(emptyList<ValutePresentation>())
    val data = MutableStateFlow("")

    fun getList(localDate: LocalDate) = viewModelScope.launch(Dispatchers.IO) {
        Thread.currentThread().name
        data.value = localDate.toString()
        val lastFiveDay = LinkedHashSet<IVarCus>()
        var i = 0
        var count = 0
        while (i < 5 && count < 16) {
            val varCus =
                ExchangeRatesRepository.getExchangeRates(ExchangeRatesDataProperty(localDate.minusDays(count.toLong())))
            if (lastFiveDay.add(varCus)) i++
            count++
        }
        list.value = lastFiveDay.first().valutes.mapIndexed { index, it ->
            with(it) {
                ValutePresentation(id = index,
                    nominal = nominal.toString(),
                    value = "$value ₽",
                    charCode = charCode,
                    name = name,
                    numCode = numCode,
                    changingLastFiveDay = lastFiveDay.map { c ->
                        DateValue(
                            date = c.date.run { "$dayOfMonth.$monthValue" }, value = c.valutes[index].value
                        )
                    }.reversed(),
                    country = ValuteFlagAlternative.values().find { (it.valute == charCode).also(::println) }
                )
            }
        }
    }
}