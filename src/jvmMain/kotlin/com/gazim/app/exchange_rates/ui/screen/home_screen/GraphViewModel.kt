package com.gazim.app.exchange_rates.ui.screen.home_screen

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class GraphViewModel(private val dateValueList: List<DateValue>) {
    private var width: Int = 0
    private var height: Int = 0
    private val viewModelScope = CoroutineScope(Dispatchers.Default)
    private var job: Job? = null
    val state = MutableStateFlow(
        GraphData(
            list = emptyList(), points = emptyList(), lines = emptyList(), max = 0f, min = 0f
        )
    )

    fun calculateGraph(width: Float, height: Float): Job {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.Default) {
            val ls = dateValueList.map(DateValue::value)
            val currentWidth = width.toInt()
            val currentHeight = height.toInt()
            if (currentWidth == this@GraphViewModel.width &&
                currentHeight == this@GraphViewModel.height
            ) return@launch
            val xs = separate(ls.size, currentWidth)
            val l = List(ls.size) { Point(x = xs[it].toFloat(), ls[it]) }
            val lagrange = LagrangePolynomial(l)
            var max = Float.MIN_VALUE
            var min = Float.MAX_VALUE
            val current = (xs.first()..xs.last()).map {
                val x = it.toFloat()
                val y = lagrange.f(x)
                println(y)
                if (max < y) max = y
                if (min > y) min = y
                println("min: $min")
                Point(x = x, y = y)
            }
            println(max)
            println(min)
            val offs = max - min
            println(offs)
            println(currentHeight)
            val li = current.map {
                println(it)
                println(it.y - min)
                it.copy(y = ((it.y - min) / offs) * currentHeight)
            }
            println(li)
            state.value = GraphData(
                list = li,
                points = li.filter { xs.contains(it.x.roundToInt()) }.mapIndexed { i, offset ->
                    GraphPoints(
                        date = dateValueList[i].date,
                        value = dateValueList[i].value.toString(),
                        x = offset.x,
                        y = offset.y
                    )
                }.also { println(it) },
                lines = separate(5, currentHeight).map { it.toFloat() },
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