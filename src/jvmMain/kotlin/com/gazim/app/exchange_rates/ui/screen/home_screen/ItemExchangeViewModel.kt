package com.gazim.app.exchange_rates.ui.screen.home_screen

import com.gazim.app.exchange_rates.math.LagrangePolynomial
import com.gazim.app.exchange_rates.ui.model.DateValue
import com.gazim.app.exchange_rates.ui.model.GraphData
import com.gazim.app.exchange_rates.ui.model.GraphPoints
import com.gazim.app.exchange_rates.math.Point
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ItemExchangeViewModel(private val dateValueList: List<DateValue>) {
    private var width: Int = 0
    private var height: Int = 0
    private val viewModelScope = CoroutineScope(Dispatchers.Default)
    private var job: Job? = null
    val state = MutableStateFlow(
        GraphData(
            list = emptyList(), points = emptyList(), lines = emptyList(), max = 0f, min = 0f
        )
    )
    private var canCalculate = dateValueList.size > 1

    fun calculateGraph(width: Float, height: Float, offsetY: Float): Job {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.Default) {
            if (!canCalculate) return@launch
            val ls = dateValueList.map(DateValue::value)
            val currentWidth = width.toInt()
            val currentHeight = height.toInt()
            if (currentWidth == this@ItemExchangeViewModel.width &&
                currentHeight == this@ItemExchangeViewModel.height
            ) return@launch
            val xs = separate(ls.size, currentWidth)
            val l = List(ls.size) { Point(x = xs[it].toFloat(), ls[it]) }
            val lagrange = LagrangePolynomial(l)
            var max = Float.MIN_VALUE
            var min = Float.MAX_VALUE
            val current = (xs.first()..xs.last()).map {
                val x = it.toFloat()
                val y = lagrange.f(x)
                if (max < y) max = y
                if (min > y) min = y
                Point(x = x, y = y)
            }
            val offs = max - min
            val li = current.map {
                it.copy(y = currentHeight - ((it.y - min) / offs) * currentHeight + offsetY)
            }
            state.value = GraphData(
                list = li,
                points = li.filter { xs.contains(it.x.roundToInt()) }.mapIndexed { i, offset ->
                    GraphPoints(
                        date = dateValueList[i].date,
                        value = dateValueList[i].value.toString(),
                        x = offset.x,
                        y = offset.y
                    )
                },
                lines = separate(5, currentHeight).map { it.toFloat() + offsetY },
                max = max,
                min = min
            )
        }
        return job!!
    }


    private fun separate(size: Int, length: Int): List<Int> {
        val step = length.toFloat() / (size - 1)
        return List(size) { (step * it).roundToInt() }
    }


}