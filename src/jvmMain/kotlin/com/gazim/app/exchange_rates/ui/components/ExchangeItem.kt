package com.gazim.app.exchange_rates.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gazim.app.exchange_rates.resource.Resources
import com.gazim.app.exchange_rates.ui.model.AspectRatio
import com.gazim.app.exchange_rates.ui.model.ExchangeFlag
import com.gazim.app.exchange_rates.ui.model.ExchangePresentation
import com.gazim.app.exchange_rates.ui.screen.home_screen.ItemExchangeViewModel

@OptIn(ExperimentalTextApi::class)
@Composable
fun ExchangeItem(
    exchange: ExchangePresentation,
    styleOne: TextStyle = MaterialTheme.typography.headlineLarge.copy(
        fontStyle = FontStyle.Italic, fontFamily = FontFamily.Serif
    ),
    styleTwo: TextStyle = MaterialTheme.typography.headlineSmall,
    viewModel: ItemExchangeViewModel = rememberSaveable(exchange) { ItemExchangeViewModel(exchange.changingLastFiveDay) },
    textMeasurer: TextMeasurer = rememberTextMeasurer(),
    dateStyle: TextStyle = MaterialTheme.typography.labelSmall
) {
    val localDensity = LocalDensity.current
    val textMeasure = rememberTextMeasurer()
    val textResultSize = remember {
        textMeasure.measure(buildAnnotatedString {
            append("##########")
        }, dateStyle).size
    }
    val graphTextWidth = remember {
        with(localDensity) {
            textResultSize.width.toDp()
        }
    }
    val graphWidth = remember { graphTextWidth * 6 }
    val heightFlag = remember {
        val empty = buildAnnotatedString {
            append("")
        }
        val pxs = textMeasure.measure(empty, styleOne).size.height + textMeasure.measure(empty, styleTwo).size.height
        with(localDensity) { pxs.toDp() + 8.dp }
    }
    val lineColor = MaterialTheme.colorScheme.primary
    val pointColor = MaterialTheme.colorScheme.secondary
    val markupLineColor = MaterialTheme.colorScheme.surfaceVariant
    val graphData by viewModel.state.collectAsState()
    ElevatedCard {
        Row {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                ItemFlag(exchange.country, AspectRatio.FourToThree, heightFlag)
                Spacer(Modifier.width(16.dp))
                ItemText(Modifier.weight(1f), styleOne, styleTwo, exchange)
                Spacer(Modifier.width(16.dp))
                GraphValueChanging(
                    modifier = Modifier.fillMaxHeight().width(graphWidth)
                        .background(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.shapes.small
                        ),
                    lineColor = lineColor,
                    markupLineColor = markupLineColor,
                    dateStyle = dateStyle.copy(color = MaterialTheme.colorScheme.onSurface),
                    graphData = graphData,
                    textMeasurer = textMeasurer,
                    valueStyle = dateStyle.copy(color = MaterialTheme.colorScheme.tertiary),
                    pointColor = pointColor,
                ) {
                    viewModel.calculateGraph(
                        width = it.width,
                        height = it.height - textResultSize.height * 2 - 3,
                        offsetY = textResultSize.height.toFloat() + 3
                    )
                }
            }
        }
    }
}

@Composable
fun ItemText(modifier: Modifier = Modifier, styleOne: TextStyle, styleTwo: TextStyle, exchange: ExchangePresentation) =
    with(exchange) {
        Column(modifier) {
            Row(Modifier.fillMaxWidth()) {
                Text("$nominal $charCode", style = styleOne)
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.width(16.dp))
                Text(value, style = styleOne, maxLines = 1)
            }
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth()) {
                Text(name, style = styleTwo)
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.width(16.dp))
                Text(numCode, style = styleTwo, maxLines = 1)
            }
        }
    }

@Composable
fun ItemFlag(exchangeFlag: ExchangeFlag?, aspectRatio: AspectRatio, heightFlag: Dp) {
    val localDensity = LocalDensity.current
    var painter by remember { mutableStateOf<Painter?>(null) }
    val width = remember {
        heightFlag * aspectRatio.widthOnHeight()
    }
    LaunchedEffect(exchangeFlag) {
        exchangeFlag?.run {
            Resources.getAndUseInputStream(getPath(aspectRatio)) { inputStream ->
                inputStream?.let { painter = loadSvgPainter(it, density = localDensity) }
            }
        }
    }
    Box(
        modifier = Modifier.height(heightFlag).width(width)
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clipToBounds(),
        contentAlignment = Alignment.Center
    ) {
        painter?.let {
            Image(
                painter = it,
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        }
    }
}