package com.gazim.app.exchange_rates.ui.screen.home_screen

import com.gazim.app.exchange_rates.ui.model.DateValue
import com.gazim.app.exchange_rates.ui.model.ExchangePresentation
import com.gazim.app.exchange_rates.ui.model.HomeScreenState
import com.gazim.app.exchange_rates.ui.model.NetworkResultStatus
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
    private val viewModelScope = CoroutineScope(Job() + Dispatchers.IO)
    val homeScreenState = MutableStateFlow(
        HomeScreenState(
            date = LocalDate.now(),
            strDate = "",
            nameVarCus = "",
            isLoaded = NetworkResultStatus.Loading,
            exchanges = emptyList(),
        )
    )

    fun getList(localDate: LocalDate) = viewModelScope.launch(Dispatchers.IO) {
        runCatching {
            println("Test")
            if (homeScreenState.value.date == localDate && homeScreenState.value.exchanges.isNotEmpty()) return@runCatching
            homeScreenState.value = homeScreenState.value.copy(
                isLoaded = NetworkResultStatus.Loading,
                date = localDate,
                strDate = localDate.toString()
            )
            val lastFiveDay = LinkedHashSet<IVarCus>()
            var i = 0
            var count = 0
            println("Start")
            while (i < 5 && count < 16) {
                val varCus =
                    ExchangeRatesRepository.getExchangeRates(ExchangeRatesDataProperty(localDate.minusDays(count.toLong())))
                println(varCus)
                if (lastFiveDay.add(varCus)) i++
                count++
            }
            val varCus = lastFiveDay.first()
            val list = varCus.exchanges.mapIndexed { index, it ->
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
                        country = ExchangeFlag.values().find { it.exchange == charCode }
                    )
                }
            }
            homeScreenState.value = varCus.run {
                HomeScreenState(
                    date = date,
                    strDate = date.toString(),
                    nameVarCus = name,
                    exchanges = list,
                    isLoaded = NetworkResultStatus.Success,
                )
            }
        }.onFailure {
            homeScreenState.value = homeScreenState.value.copy(
                isLoaded = NetworkResultStatus.Failed
            )
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