package com.gazim.app.exchange_rates.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gazim.app.exchange_rates.ui.model.ExchangeItemState
import com.gazim.library.exchange_rates.model.IExchange

@OptIn(ExperimentalTextApi::class, ExperimentalFoundationApi::class)
@Composable
fun ExchangeList(exchanges: List<ExchangeItemState>, spacerHeight: Dp, onItemClick: (IExchange) -> Unit) {
    val scrollState = rememberLazyListState()
    val scrollAdapter = rememberScrollbarAdapter(scrollState)
    Box(Modifier) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp),
            state = scrollState,
        ) {
            item {
                Spacer(Modifier.height(spacerHeight - 8.dp))
            }
            items(exchanges) {
                ExchangeItem(
                    exchange = it,
                    styleOne = MaterialTheme.typography.headlineLarge,
                    styleTwo = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Light),
                    onItemClick = onItemClick
                )
            }
        }
        VerticalScrollbar(
            scrollAdapter,
            Modifier.align(Alignment.CenterEnd).fillMaxHeight().padding(end = 2.dp, top = spacerHeight),
            style = ScrollbarStyle(
                minimalHeight = 32.dp,
                thickness = 8.dp,
                shape = MaterialTheme.shapes.extraSmall,
                hoverDurationMillis = 500,
                unhoverColor = MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.3f),
                hoverColor = MaterialTheme.colorScheme.surfaceTint
            )
        )
    }

}