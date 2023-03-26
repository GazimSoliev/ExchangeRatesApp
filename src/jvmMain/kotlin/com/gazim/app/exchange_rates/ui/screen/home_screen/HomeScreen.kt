package com.gazim.app.exchange_rates.ui.screen.home_screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gazim.app.exchange_rates.resource.Resources
import com.gazim.app.exchange_rates.ui.components.Calendar
import com.gazim.app.exchange_rates.ui.components.GraphValueChanging
import com.gazim.app.exchange_rates.ui.model.AspectRatio
import com.gazim.app.exchange_rates.ui.model.ExchangeFlag
import com.gazim.app.exchange_rates.ui.model.ExchangePresentation
import com.gazim.app.exchange_rates.ui.model.HomeScreenState
import com.gazim.app.exchange_rates.ui.model.NetworkResultStatus.*
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    remember { viewModel.getList(LocalDate.now()) }
    val homeScreenState by viewModel.homeScreenState.collectAsState()
    HomeScreenComponent(
        homeScreenState,
        viewModel::getList,
        viewModel::update,
        homeScreenState.searchQuery,
        viewModel::setQuery
    )
}

@Composable
fun HomeScreenComponent(
    homeScreenState: HomeScreenState,
    onDateChange: (LocalDate) -> Unit,
    onRefresh: () -> Unit,
    text: String,
    onTextChange: (String) -> Unit
) {
    val localDensity = LocalDensity.current
    var spacerHeight by remember { mutableStateOf(0.dp) }
    Box(contentAlignment = Alignment.Center) {
        when (homeScreenState.isLoaded) {
            Loading -> CircularProgressIndicator()
            Success -> ExchangeList(homeScreenState.exchanges, spacerHeight)
            Failed -> Text(homeScreenState.errorMsg)
        }
        Column(Modifier.align(Alignment.TopCenter).onGloballyPositioned {
            with(localDensity) {
                spacerHeight = it.size.height.toDp()
            }
        }) {
            Spacer(Modifier.height(16.dp))
            CustomTopBar(
                Modifier,
                homeScreenState.date,
                homeScreenState.strDate,
                onDateChange,
                onRefresh,
                text,
                onTextChange
            )
        }
    }
}

@Composable
fun CustomTopBarItem(modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxHeight().background(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.shapes.extraLarge
        ),
        content = content
    )
}

@Composable
fun CustomTopBar(
    modifier: Modifier,
    date: LocalDate,
    dateStr: String,
    onNewDate: (LocalDate) -> Unit,
    onRefresh: () -> Unit,
    text: String,
    onTextChange: (String) -> Unit
) {
    val dialogState = rememberMaterialDialogState()
    Calendar(dialogState, date, onPositive = onNewDate)
    val onSurfaceVariant = MaterialTheme.colorScheme.onSecondaryContainer
    val scope = rememberCoroutineScope()
    var selection by remember { mutableStateOf(TextRange.Zero) }
    val focusRequester = remember { FocusRequester() }
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomTopBarItem {
            Box(Modifier.aspectRatio(1f).fillMaxHeight().padding(4.dp), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Search, "Search")
            }
            BasicTextField(
                modifier = Modifier.focusRequester(focusRequester),
                value = TextFieldValue(text, selection),
                onValueChange = { selection = it.selection; onTextChange(it.text) },
                textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
            )
        }
        CustomTopBarItem {
            IconButton(onClick = {
                scope.launch {
                    delay(300)
                    dialogState.show()
                }
            }) {
                Icon(Icons.Default.EditCalendar, "Choose a date")
            }
            Text(dateStr, color = onSurfaceVariant)
            Spacer(Modifier.padding(8.dp))
        }
        CustomTopBarItem {
            FilledTonalIconButton(onClick = { onRefresh() }) {
                Icon(Icons.Default.Refresh, "Refresh list", tint = onSurfaceVariant)
            }
        }
    }
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
fun ItemFlag(exchangeFlag: ExchangeFlag?, aspectRatio: AspectRatio, heightFlag: Dp) {
    val localDensity = LocalDensity.current
    var painter by remember { mutableStateOf<Painter?>(null) }
    val width = remember {
        heightFlag * when (aspectRatio) {
            AspectRatio.FourToThree -> 4f / 3f
            AspectRatio.OneToOne -> 1f
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