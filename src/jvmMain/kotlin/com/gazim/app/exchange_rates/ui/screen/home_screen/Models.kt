package com.gazim.app.exchange_rates.ui.screen.home_screen

data class ValutePresentation(
    val id: Int,
    val nominal: String,
    val value: String,
    val charCode: String,
    val name: String,
    val numCode: String,
    val changingLastFiveDay: List<DateValue>
)

data class DateValue(
    val date: String,
    val value: Float
)

data class VarCusPresentation(
    val date: String,
    val name: String,
    val valutes: List<ValutePresentation>
)

data class GraphData(
    val list: List<Point>,
    val points: List<GraphPoints>,
    val lines: List<Float>,
    val max: Float,
    val min: Float
)

data class GraphPoints(
    val date: String,
    val value: String,
    val x: Float,
    val y: Float
)

data class Point(
    val x: Float,
    val y: Float
)