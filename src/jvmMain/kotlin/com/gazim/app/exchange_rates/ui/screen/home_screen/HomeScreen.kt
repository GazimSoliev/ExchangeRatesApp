package com.gazim.app.exchange_rates.ui.screen.home_screen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.gazim.app.exchange_rates.ui.components.CustomTopBar
import com.gazim.app.exchange_rates.ui.components.ExchangeList
import com.gazim.app.exchange_rates.ui.model.HomeScreenState
import com.gazim.app.exchange_rates.ui.model.NetworkResultStatus.*
import com.gazim.library.exchange_rates.model.IExchange
import java.time.LocalDate

@Composable
fun HomeScreen(viewModel: HomeViewModel, onItemClick: (IExchange) -> Unit) {
    remember { viewModel.getList(LocalDate.now()) }
    val homeScreenState by viewModel.homeScreenState.collectAsState()
    HomeScreenComponent(
        homeScreenState,
        viewModel::getList,
        viewModel::update,
        homeScreenState.searchQuery,
        viewModel::setQuery,
        onItemClick
    )
}

@Composable
fun HomeScreenComponent(
    homeScreenState: HomeScreenState,
    onDateChange: (LocalDate) -> Unit,
    onRefresh: () -> Unit,
    text: String,
    onTextChange: (String) -> Unit,
    onItemClick: (IExchange) -> Unit
) {
    var calendarIsOpened by remember { mutableStateOf(false) }
    val blurDp by animateDpAsState(
        if (calendarIsOpened) 16.dp else 0.dp,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )
    val localDensity = LocalDensity.current
    var spacerHeight by remember { mutableStateOf(0.dp) }
    Box(contentAlignment = Alignment.Center, modifier = Modifier.blur(blurDp)) {
        when (homeScreenState.isLoaded) {
            Loading -> CircularProgressIndicator()
            Success -> ExchangeList(homeScreenState.exchanges, spacerHeight, onItemClick = onItemClick)
            Failed -> Text(homeScreenState.errorMsg)
        }
        Box(Modifier.align(Alignment.TopCenter).onGloballyPositioned {
            with(localDensity) {
                spacerHeight = it.size.height.toDp()
            }
        }) {
            CustomTopBar(
                Modifier,
                homeScreenState.date,
                homeScreenState.strDate,
                onDateChange,
                onRefresh,
                text,
                onTextChange
            ) {
                calendarIsOpened = it
            }
        }
    }
}

