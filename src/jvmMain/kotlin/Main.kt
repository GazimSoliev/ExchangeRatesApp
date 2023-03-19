import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.gazim.app.exchange_rates.ui.screen.home_screen.HomeScreen
import com.gazim.app.exchange_rates.ui.screen.home_screen.HomeViewModel
import com.gazim.app.exchange_rates.ui.theme.AppTheme

//@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
//@Composable
//@Preview
//fun App() {
//    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
//    val nestedScrollCon = remember { MyNestedScrollCon(scrollBehavior.nestedScrollConnection) }
//    MaterialTheme {
//        Scaffold(
//            topBar = {
//                LargeTopAppBar(
//                    title = {
//                        Text(
//                            "Centered TopAppBar",
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis
//                        )
//                    },
//                    navigationIcon = {
//                        IconButton(onClick = { /* doSomething() */ }) {
//                            Icon(
//                                imageVector = Icons.Filled.Menu,
//                                contentDescription = "Localized description"
//                            )
//                        }
//                    },
//                    actions = {
//                        IconButton(onClick = { /* doSomething() */ }) {
//                            Icon(
//                                imageVector = Icons.Filled.Favorite,
//                                contentDescription = "Localized description"
//                            )
//                        }
//                    },
//                    scrollBehavior = scrollBehavior
//                )
//            },
//            modifier = Modifier.nestedScroll(nestedScrollCon)
//                .onPointerEvent(PointerEventType.Scroll) {
//                    nestedScrollCon.onPostScroll(
//                        it.changes.first().scrollDelta,
//                        Offset(0f, 0f),
//                        NestedScrollSource.Drag
//                    )
//                },
//            content = { innerPadding ->
//                LazyColumn(
//                    contentPadding = innerPadding,
//                    verticalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    val list = (0..75).map { it.toString() }
//                    items(count = list.size) {
//                        Text(
//                            text = list[it],
//                            style = MaterialTheme.typography.bodyLarge,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 16.dp)
//                        )
//                    }
//                }
//                Canvas(Modifier) {
////                    drawPath()
//                }
//            }
//        )
//    }
//}
//
//class MyNestedScrollCon(val nestedScrollConnection: NestedScrollConnection) : NestedScrollConnection {
//    override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
//        val res = nestedScrollConnection.onPostScroll(consumed, available, source)
//        println("onPostScroll: $consumed, $available, $source\n$res")
//        return res
//    }
//    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
//        val res = nestedScrollConnection.onPreScroll(available, source)
//        println("onPreScroll: $available, $source\n$res")
//        return res
//    }
//
//    override suspend fun onPreFling(available: Velocity): Velocity {
//        val res = nestedScrollConnection.onPreFling(available)
//        println("onPreFling: $available\n$res")
//        return res
//    }
//
//    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
//        val res = nestedScrollConnection.onPostFling(consumed, available)
//        println("onPostFling: $consumed, $available\n$res")
//        return res
//    }
//
//}


fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        ExchangeRatesApp()
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun ExchangeRatesApp() {
    AppTheme {
        Surface(Modifier.fillMaxSize()) {
            HomeScreen(remember { HomeViewModel() })
        }
    }
}
//
//@Preview
//@Composable
//fun Test() {
//    @Composable
//    fun DrawCubic() {
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(rememberScrollState())
//        ) {
//
//            val density = LocalDensity.current.density
//
//            val screenWidth = 400.dp
//
//            val screenWidthInPx = screenWidth.value * density
//
//            // (x0, y0) is initial coordinate where path is moved with path.moveTo(x0,y0)
//            var x0 by remember { mutableStateOf(0f) }
//            var y0 by remember { mutableStateOf(0f) }
//
//            /*
//            Adds a cubic bezier segment that curves from the current point(x0,y0) to the
//            given point (x3, y3), using the control points (x1, y1) and (x2, y2).
//         */
//            var x1 by remember { mutableStateOf(0f) }
//            var y1 by remember { mutableStateOf(screenWidthInPx) }
//            var x2 by remember { mutableStateOf(screenWidthInPx/2) }
//            var y2 by remember { mutableStateOf(0f) }
//
//            var x3 by remember { mutableStateOf(screenWidthInPx) }
//            var y3 by remember { mutableStateOf(screenWidthInPx/2) }
//
//            val path = remember { Path() }
//            Canvas(
//                modifier = Modifier
//                    .padding(8.dp)
//                    .shadow(1.dp)
//                    .background(Color.White)
//                    .size(screenWidth, screenWidth/2)
//            ) {
//                path.reset()
//                path.moveTo(x0, y0)
//                path.cubicTo(x1 = x1, y1 = y1, x2 = x2, y2 = y2, x3 = x3, y3 = y3)
//
//
//                drawPath(
//                    color = Color.Green,
//                    path = path,
//                    style = Stroke(
//                        width = 3.dp.toPx(),
//                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
//                    )
//                )
//
//                // Draw Control Points on screen
//                drawPoints(
//                    listOf(Offset(x1, y1), Offset(x2, y2)),
//                    color = Color.Green,
//                    pointMode = PointMode.Points,
//                    cap = StrokeCap.Round,
//                    strokeWidth = 40f
//                )
//            }
//
//            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
//
//                Text(text = "X0: ${x0.roundToInt()}")
//                Slider(
//                    value = x0,
//                    onValueChange = { x0 = it },
//                    valueRange = 0f..screenWidthInPx,
//                )
//
//                Text(text = "Y0: ${y0.roundToInt()}")
//                Slider(
//                    value = y0,
//                    onValueChange = { y0 = it },
//                    valueRange = 0f..screenWidthInPx,
//                )
//
//                Text(text = "X1: ${x1.roundToInt()}")
//                Slider(
//                    value = x1,
//                    onValueChange = { x1 = it },
//                    valueRange = 0f..screenWidthInPx,
//                )
//
//                Text(text = "Y1: ${y1.roundToInt()}")
//                Slider(
//                    value = y1,
//                    onValueChange = { y1 = it },
//                    valueRange = 0f..screenWidthInPx,
//                )
//
//                Text(text = "X2: ${x2.roundToInt()}")
//                Slider(
//                    value = x2,
//                    onValueChange = { x2 = it },
//                    valueRange = 0f..screenWidthInPx,
//                )
//
//                Text(text = "Y2: ${y2.roundToInt()}")
//                Slider(
//                    value = y2,
//                    onValueChange = { y2 = it },
//                    valueRange = 0f..screenWidthInPx,
//                )
//
//                Text(text = "X3: ${x3.roundToInt()}")
//                Slider(
//                    value = x3,
//                    onValueChange = { x3 = it },
//                    valueRange = 0f..screenWidthInPx,
//                )
//
//                Text(text = "Y3: ${y3.roundToInt()}")
//                Slider(
//                    value = y3,
//                    onValueChange = { y3 = it },
//                    valueRange = 0f..screenWidthInPx,
//                )
//            }
//        }
//    }
//}


data class Person(
    val name: String,
    val surname: String
)