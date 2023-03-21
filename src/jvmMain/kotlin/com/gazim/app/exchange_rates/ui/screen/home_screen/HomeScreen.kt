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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gazim.app.exchange_rates.resource.Resources
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
    val localDensity = LocalDensity.current
    val scrollState = rememberLazyListState()
    val scrollAdapter = rememberScrollbarAdapter(scrollState)
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
                    styleTwo = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Light)
                )
            }
        }
        VerticalScrollbar(
            scrollAdapter,
            Modifier.align(Alignment.CenterEnd).fillMaxHeight().padding(3.25.dp)
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
    viewModel: ItemValuteViewModel = rememberSaveable(valute) { ItemValuteViewModel(valute.changingLastFiveDay) },
    textMeasurer: TextMeasurer = rememberTextMeasurer(),
    dateStyle: TextStyle = MaterialTheme.typography.labelSmall
) {
    val localDensity = LocalDensity.current
    val textMeasure = rememberTextMeasurer()
    val textResultSize = remember {
        println("Init")
        textMeasure.measure(buildAnnotatedString {
            append("###.#### ")
        }, dateStyle).size
    }
    val graphTextWidth = remember {
        with(localDensity) {
            textResultSize.width.toDp()
        }
    }
    val graphWidth = remember { graphTextWidth * 5 }
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
                ItemFlag(valute.country, ValuteFlagAlternative.AspectRatio.FourToThree, heightFlag)
                Spacer(Modifier.width(16.dp))
                ItemText(Modifier.weight(1f), styleOne, styleTwo, valute)
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
fun ItemText(modifier: Modifier = Modifier, styleOne: TextStyle, styleTwo: TextStyle, valute: ValutePresentation) =
    with(valute) {
        Column(modifier) {
            Row(Modifier.fillMaxWidth()) {
                Text("$nominal $charCode", style = styleOne)
                Spacer(Modifier.weight(1f))
                Text(value, style = styleOne)
            }
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth()) {
                Text(name, style = styleTwo)
                Spacer(Modifier.weight(1f))
                Text(numCode, style = styleTwo)
            }
        }
    }

@Composable
fun ItemFlag(valuteFlag: ValuteFlagAlternative?, aspectRatio: ValuteFlagAlternative.AspectRatio, heightFlag: Dp) {
    val localDensity = LocalDensity.current
    var painter by remember { mutableStateOf<Painter?>(null) }
    val width = remember {
        heightFlag * when (aspectRatio) {
            ValuteFlagAlternative.AspectRatio.FourToThree -> 4f / 3f
            ValuteFlagAlternative.AspectRatio.OneToOne -> 1f
            else -> 0f
        }
    }
    LaunchedEffect(valuteFlag) {
        valuteFlag?.run {
            Resources.getAndUseInputStream(getPath(aspectRatio)) { inputStream ->
                inputStream?.let { painter = loadSvgPainter(it, density = localDensity) }
            }
        }
    }
    Box(
        modifier = Modifier.height(heightFlag).width(width)
//            .background(MaterialTheme.colorScheme.surfaceVariant)
        ,
        contentAlignment = Alignment.Center
    ) {
        painter?.let {
//            Image(
//                painter = it,
//                contentDescription = "",
//                modifier = Modifier.blur(16.dp).alpha(0.5f),
//                contentScale = ContentScale.Crop
//            )
            Image(
                painter = it,
                contentDescription = "",
                modifier = Modifier.background(Color.Red).fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        }
    }
}