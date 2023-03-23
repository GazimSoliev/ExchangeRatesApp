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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.gazim.app.exchange_rates.resource.Resources
import com.gazim.app.exchange_rates.ui.components.Calendar
import com.gazim.app.exchange_rates.ui.components.GraphValueChanging
import com.gazim.app.exchange_rates.ui.model.ExchangePresentation
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    remember { viewModel.getList(LocalDate.now()) }
    val list by viewModel.list.collectAsState()
    val date by viewModel.data.collectAsState()
    HomeScreenComponent(date, list) {
        viewModel.getList(it)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreenComponent(
    date: LocalDate,
    exchanges: List<ExchangePresentation>,
    onDateChange: (LocalDate) -> Unit
) {
    val dialogState = rememberMaterialDialogState()
    Calendar(dialogState, date, onPositive = onDateChange)
    val localDensity = LocalDensity.current
    var text by remember { mutableStateOf("") }
    var spacerHeight by remember { mutableStateOf(0.dp) }
    Box {
//        Row(
//            modifier = Modifier.background(MaterialTheme.colorScheme.surface).fillMaxWidth().padding(8.dp),
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Text(date)
//            Spacer(modifier = Modifier.weight(1f))
//            OutlinedTextField(text, { text = it })
//            Spacer(modifier = Modifier.weight(1f))
//        }

        ExchangeList(exchanges, spacerHeight)
        Column(Modifier.onGloballyPositioned {
            with(localDensity) {
                spacerHeight = it.size.height.toDp()
            }
        }) {
            Spacer(Modifier.height(16.dp))
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
//                Box(
//                    modifier = Modifier.background(
//                        MaterialTheme.colorScheme.secondaryContainer,
//                        MaterialTheme.shapes.extraLarge
//                    ).padding(8.dp).onClick {
//                        dialogState.show()
//                    }
//                ) {
//                    Text(date.toString(), color = MaterialTheme.colorScheme.onSecondaryContainer)
//                }
                val scope = rememberCoroutineScope()
                FilledTonalButton(
                    onClick = {
                        scope.launch {
                            delay(300)
                            dialogState.show()
                        }
                    }
                ) {
                    Text(date.toString(), color = MaterialTheme.colorScheme.onSecondaryContainer)
                }
            }
        }
    }
}

@Preview
@Composable
fun ExchangeListPreview() {
//    ValuteList(listOf(null, null, null))
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun ExchangeList(exchanges: List<ExchangePresentation>, spacerHeight: Dp) {
    val scrollState = rememberLazyListState()
    val scrollAdapter = rememberScrollbarAdapter(scrollState)
    Box(Modifier) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp),
            state = scrollState
        ) {
            item {
                Spacer(Modifier.height(spacerHeight))
            }
            items(exchanges) {
                Item(
                    exchange = it,
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
fun ItemFlag(exchangeFlag: ExchangeFlagAlternative?, aspectRatio: AspectRatio, heightFlag: Dp) {
    val localDensity = LocalDensity.current
    var painter by remember { mutableStateOf<Painter?>(null) }
    val width = remember {
        heightFlag * when (aspectRatio) {
            AspectRatio.FourToThree -> 4f / 3f
            AspectRatio.OneToOne -> 1f
            else -> 0f
        }
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