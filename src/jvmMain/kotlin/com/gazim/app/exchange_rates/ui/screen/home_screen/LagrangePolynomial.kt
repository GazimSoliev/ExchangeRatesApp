package com.gazim.app.exchange_rates.ui.screen.home_screen

import com.gazim.app.exchange_rates.ui.model.Point


@Suppress("MemberVisibilityCanBePrivate")
class LagrangePolynomial(points: List<Point>) {

    private val listOfX = points.map(Point::x)
    private val listOfY = points.map(Point::y)

    fun p(i: Int, x: Float): Float {
        val mutableList = listOfX.toMutableList()
        val removedX = mutableList.removeAt(i)
        val c = mutableList.multiplyOf { removedX - it }
        return mutableList.multiplyOf { x - it } / c
    }

    fun l(x: Float): Float {
        var r = 0f
        listOfY.forEachIndexed { i, y -> r += p(i, x) * y }
        return r
    }

    fun f(x: Number) = l(x.toFloat())

    private inline fun Iterable<Float>.multiplyOf(selector: (Float) -> Float): Float {
        var r = 1f
        for (i in this) r *= selector(i)
        return r
    }
}