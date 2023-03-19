package com.gazim.app.exchange_rates.ui.screen.home_screen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gazim.app.exchange_rates.ui.components.GraphValueChanging
import com.gazim.app.exchange_rates.ui.model.ValutePresentation
import java.time.LocalDate

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    remember { viewModel.getList(LocalDate.now()) }
    HomeScreenComponent(viewModel.list.collectAsState().value)
}

@Composable
fun HomeScreenComponent(
    valutes: List<ValutePresentation>
) {
    ValuteList(valutes)
}

@Preview
@Composable
fun ValuteListPreview() {
//    ValuteList(listOf(null, null, null))
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun ValuteList(valutes: List<ValutePresentation>) {
    val scrollState = rememberLazyListState()
    val scrollAdapter = rememberScrollbarAdapter(scrollState)
    val textMeasure = rememberTextMeasurer()
    Box(Modifier) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp),
            state = scrollState
        ) {
            items(valutes) {
                Item(
                    valute = it,
                    styleOne = MaterialTheme.typography.headlineLarge,
                    styleTwo = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Light),
                    textMeasurer = textMeasure
                )
            }
        }
        VerticalScrollbar(
            scrollAdapter,
            Modifier.align(Alignment.CenterEnd).fillMaxHeight().padding(3.dp)
        )
    }

}

@OptIn(ExperimentalTextApi::class)
@Composable
fun Item(
    valute: ValutePresentation,
    styleOne: TextStyle = MaterialTheme.typography.headlineLarge.copy(
        fontStyle = FontStyle.Italic, fontFamily = FontFamily.Serif
    ),
    styleTwo: TextStyle = MaterialTheme.typography.headlineSmall,
    viewModel: GraphViewModel = rememberSaveable(valute) { GraphViewModel(valute.changingLastFiveDay) },
    textMeasurer: TextMeasurer = rememberTextMeasurer()
) {
    val datesStyle = MaterialTheme.typography.labelSmall
    val textHeight = LocalDensity.current.run { datesStyle.fontSize.toPx() }
    val lineColor = MaterialTheme.colorScheme.primary
    val pointColor = MaterialTheme.colorScheme.secondary
    val markupLineColor = MaterialTheme.colorScheme.surfaceVariant
    val graphData by viewModel.state.collectAsState()
    val height = 8.dp
    ElevatedCard {
        with(valute) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(8.dp)
            ) {
                Column(Modifier.weight(1f)) {
                    Row(Modifier.fillMaxWidth()) {
                        Text("$nominal $charCode", style = styleOne)
                        Spacer(Modifier.weight(1f))
                        Text(value, style = styleOne)
                    }
                    Spacer(Modifier.height(height))
                    Row(Modifier.fillMaxWidth()) {
                        Text(name, style = styleTwo)
                        Spacer(Modifier.weight(1f))
                        Text(numCode, style = styleTwo)
                    }
                }
                Spacer(Modifier.width(16.dp))
                GraphValueChanging(
                    modifier = Modifier.weight(0.25f).fillMaxHeight()
                        .background(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.shapes.small
                        ),
                    lineColor = lineColor,
                    markupLineColor = markupLineColor,
                    dateStyle = datesStyle.copy(color = MaterialTheme.colorScheme.onSurface),
                    graphData = graphData,
                    textMeasurer = textMeasurer,
                    pointColor = pointColor
                ) {
                    viewModel.calculateGraph(width = it.width, height = it.height - textHeight * 2, offsetY = 0f)
                }
            }
        }
    }
}

@Preview
@Composable
fun ItemPreview() {

}