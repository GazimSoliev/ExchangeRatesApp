package com.gazim.app.exchange_rates.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.dp
import com.gazim.app.exchange_rates.ui.model.GraphData


@OptIn(ExperimentalTextApi::class)
@Composable
inline fun GraphValueChanging(
    modifier: Modifier,
    lineColor: Color,
    markupLineColor: Color,
    pointColor: Color,
    dateStyle: TextStyle,
    valueStyle: TextStyle,
    graphData: GraphData,
    textMeasurer: TextMeasurer,
    crossinline sizeMeasure: (Size) -> Unit
) {
    Canvas(
        modifier.padding(vertical = 8.dp, horizontal = 32.dp)
    ) {
        sizeMeasure(size)
//        graphData.lines.forEach {
//            drawLine(markupLineColor, start = Offset(x = 0f, y = it), end = Offset(x = size.width, y = it))
//        }
        drawPoints(
            points = graphData.list.map { Offset(it.x, it.y) },
            color = lineColor,
            pointMode = PointMode.Polygon,
            cap = StrokeCap.Round
        )
        drawPoints(
            points = graphData.points.map { Offset(it.x, it.y) },
            color = pointColor,
            pointMode = PointMode.Points, strokeWidth = 5f,
            cap = StrokeCap.Round
        )
        graphData.points.forEach {
            val textDate = textMeasurer.measure(
                text = buildAnnotatedString { append(it.date) },
                style = dateStyle,
                maxLines = 1
            )
            val textValue = textMeasurer.measure(
                text = buildAnnotatedString {
                    append(it.value)
                },
                style = valueStyle,
                maxLines = 1
            )
            drawText(
                textLayoutResult = textDate,
                topLeft = Offset(
                    it.x - (textDate.size.width / 2),
                    size.height - textDate.size.height + 5
                )
            )
            drawText(
                textLayoutResult = textValue, topLeft = Offset(
                    it.x - textValue.size.width / 2, 0f
                )
            )
        }
    }
}