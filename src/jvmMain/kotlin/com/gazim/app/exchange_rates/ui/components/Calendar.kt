package com.gazim.app.exchange_rates.ui.components

import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogProperties
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.time.LocalDate

@Composable
fun Calendar(
    dialogState: MaterialDialogState,
    initDate: LocalDate,
    onPositive: (LocalDate) -> Unit,
    onDialogClose: () -> Unit
) {
    val textButtonColors = ButtonDefaults.textButtonColors(
        backgroundColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.primary,
        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled)
    )
    MaterialDialog(
        dialogState = dialogState,
        properties = MaterialDialogProperties(size = DpSize(300.dp, 550.dp), title = "Календарь"),
        buttons = {
            positiveButton("Ок", colors = textButtonColors, onClick = onDialogClose)
            negativeButton("Отмена", colors = textButtonColors, onClick = onDialogClose)
        },
        backgroundColor = MaterialTheme.colorScheme.background,
        onCloseRequest = { it.hide(); onDialogClose() }
    ) {
        datepicker(
            initialDate = initDate.toKotlinLocalDate(),
            title = "Выберите дату",
            locale = Locale("ru"),
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colorScheme.primary,
                headerTextColor = MaterialTheme.colorScheme.onPrimary,
                calendarHeaderTextColor = MaterialTheme.colorScheme.onBackground,
                dateActiveBackgroundColor = MaterialTheme.colorScheme.primary,
                dateInactiveBackgroundColor = Color.Transparent,
                dateActiveTextColor = MaterialTheme.colorScheme.onPrimary,
                dateInactiveTextColor = MaterialTheme.colorScheme.onBackground
            )
        ) { date ->
            onPositive(date.toJavaLocalDate())
        }
    }
}