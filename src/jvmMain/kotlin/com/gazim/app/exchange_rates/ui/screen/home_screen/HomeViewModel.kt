package com.gazim.app.exchange_rates.ui.screen.home_screen

import com.gazim.app.exchange_rates.ui.model.*
import com.gazim.library.exchange_rates.model.ExchangeRatesDataProperty
import com.gazim.library.exchange_rates.model.IVarCus
import com.gazim.library.exchange_rates.repository.ExchangeRatesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeViewModel {
    private val viewModelScope = CoroutineScope(Job())
    private var clearList = emptyList<ExchangePresentation>()
    val homeScreenState = MutableStateFlow(
        HomeScreenState(
            date = LocalDate.now(),
            strDate = "",
            nameVarCus = "",
            isLoaded = NetworkResultStatus.Loading,
            exchanges = emptyList(),
            searchQuery = ""
        )
    )
    private var _homeScreenState
        get() = homeScreenState.value
        set(value) {
            homeScreenState.value = value
        }

    fun setQuery(query: String) = viewModelScope.launch {
        _homeScreenState = _homeScreenState.copy(
            searchQuery = query,
            exchanges = filter(query)
        )
    }

    fun getList(localDate: LocalDate) = viewModelScope.launch(Dispatchers.IO) {
        if (
            homeScreenState.value.date == localDate && clearList.isNotEmpty()
        ) return@launch
        homeScreenState.value = homeScreenState.value.copy(
            date = localDate,
            strDate = localDate.format()
        )
        update()
    }

    private fun LocalDate.format(): String = format(DateTimeFormatter.ofPattern("dd.LL.YYYY"))
    private fun LocalDate.shortFormat(): String = format(DateTimeFormatter.ofPattern("dd.LL"))

    fun update() = viewModelScope.launch(Dispatchers.IO) {
        runCatching {
            homeScreenState.value = homeScreenState.value.copy(
                isLoaded = NetworkResultStatus.Loading
            )
            val localDate = homeScreenState.value.date
            val lastFiveDay = LinkedHashSet<IVarCus>()
            var i = 0
            var count = 0
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
                                date = c.date.shortFormat(),
                                value = c.exchanges[index].value
                            )
                        }.reversed(),
                        country = ExchangeFlag.values().find { it.exchange == charCode }
                    )
                }
            }
            clearList = list
            val query = homeScreenState.value.searchQuery
            homeScreenState.value = varCus.run {
                HomeScreenState(
                    date = date,
                    strDate = date.format(),
                    nameVarCus = name,
                    exchanges = filter(query),
                    isLoaded = NetworkResultStatus.Success,
                    searchQuery = query
                )
            }
        }.onFailure {
            homeScreenState.value = homeScreenState.value.copy(
                isLoaded = NetworkResultStatus.Failed
            )
        }
    }

    private fun filter(query: String): List<ExchangePresentation> {
        return clearList.filter {
            query.isBlank() ||
                    it.value.contains(query, true) ||
                    it.charCode.contains(query, true) ||
                    it.name.contains(query, true) ||
                    it.nominal.contains(query, true) ||
                    it.country?.name?.contains(query, true) ?: false
        }
    }

}