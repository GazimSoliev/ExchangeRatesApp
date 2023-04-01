package com.gazim.app.exchange_rates.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomTopBarItem(modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxHeight().background(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.shapes.extraLarge
        ).onClick { },
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
    onTextChange: (String) -> Unit,
    showBlur: (Boolean) -> Unit
) {
    val dialogState = rememberMaterialDialogState()
    val onSurfaceVariant = MaterialTheme.colorScheme.onSecondaryContainer
    var selection by remember { mutableStateOf(TextRange.Zero) }
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    Calendar(dialogState, date, onPositive = onNewDate) {
        coroutineScope.launch {
            delay(300)
            showBlur(false)
        }
    }
    Row(
        modifier = modifier.height(IntrinsicSize.Min).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomTopBarItem {
            Box(Modifier.aspectRatio(1f).fillMaxHeight().padding(4.dp), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Search, "Search")
            }
            BasicTextField(
                modifier = Modifier.focusRequester(focusRequester).width(128.dp),
                value = TextFieldValue(text, selection),
                onValueChange = { selection = it.selection; onTextChange(it.text) },
                textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                maxLines = 1,
                singleLine = true
            )
            Spacer(Modifier.width(16.dp))
        }
        CustomTopBarItem {
            IconButton(onClick = {
                coroutineScope.launch {
                    showBlur(true)
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