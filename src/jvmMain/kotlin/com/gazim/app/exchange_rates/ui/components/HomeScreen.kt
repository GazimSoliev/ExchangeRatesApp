package com.gazim.app.exchange_rates.ui.components

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
import com.gazim.app.exchange_rates.ui.model.HomeScreenState
import com.gazim.app.exchange_rates.ui.model.NetworkResultStatus
import com.gazim.library.exchange_rates.model.IExchange
import java.time.LocalDate

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
            NetworkResultStatus.Loading -> CircularProgressIndicator()
            NetworkResultStatus.Success -> ExchangeList(
                homeScreenState.exchanges,
                spacerHeight,
                onItemClick = onItemClick
            )

            NetworkResultStatus.Failed -> Text(homeScreenState.errorMsg)
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