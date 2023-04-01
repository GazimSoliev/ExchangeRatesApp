package com.gazim.app.exchange_rates.ui.screen.home_screen

import com.gazim.app.exchange_rates.ui.model.*
import com.gazim.library.exchange_rates.model.IVarCus
import com.gazim.library.exchange_rates.model.exchange_er_property_models_impl.DateExcERProp
import com.gazim.library.exchange_rates.repository.ExchangeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class HomeViewModel {
    private val viewModelScope = CoroutineScope(Job())
    private var clearList = emptyList<ExchangeItemState>()
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
    private fun LocalDate.shortFormat(): String = format(DateTimeFormatter.ofPattern("MMM, YYYY", Locale("ru")))

    fun update() = viewModelScope.launch(Dispatchers.IO) {
        runCatching {
            homeScreenState.value = homeScreenState.value.copy(
                isLoaded = NetworkResultStatus.Loading
            )
            val localDate = homeScreenState.value.date
            val lastFiveDay = LinkedHashSet<IVarCus>()
            var i = 0
            var count = 0
            while (i < 6 && count < 16) {
                println("Started")
                val varCus =
                    ExchangeRepository.getExchanges(
                        setOf(
                            DateExcERProp(
                                localDate.minusMonths(
                                    count.times(2).toLong()
                                )
                            )
                        )
                    ).also { println(it) }
                if (lastFiveDay.add(varCus)) i++
                count++
            }
            val varCus = lastFiveDay.first()
            val list = varCus.exchanges.mapIndexed { index, it ->
                with(it) {
                    ExchangeItemState(
                        exchange = this,
                        id = index,
                        nominal = nominal.toString(),
                        value = "$value ₽",
                        charCode = charCode,
                        name = name,
                        numCode = numCode,
                        changingLastFiveDay = lastFiveDay.mapNotNull { c ->
                            val size = c.exchanges.size
                            (if (index < size) c.exchanges.find { it.charCode == charCode }?.value else null)?.let {
                                DateValue(
                                    date = c.date.shortFormat(),
                                    value = it
                                )
                            }
                        }.reversed(),
                        country = ExchangeFlag.values().find { it.exchanges.contains(charCode) }
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

    private fun filter(query: String) = clearList.filter {
        query.isBlank() ||
                it.value.contains(query, true) ||
                it.charCode.contains(query, true) ||
                it.name.contains(query, true) ||
                it.nominal.contains(query) ||
                it.numCode.contains(query) ||
                it.country?.name?.contains(query, true) ?: false
    }

}