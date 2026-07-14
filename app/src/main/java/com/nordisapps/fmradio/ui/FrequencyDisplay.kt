package com.nordisapps.fmradio.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun FrequencyDisplay(
    currentFrequency: String,
    onFrequencyConfirmed: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    Text(
        text = if (currentFrequency.isBlank()) "87.5 MHz" else "$currentFrequency MHz",
        fontSize = 56.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .padding(bottom = 16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { showDialog = true }
    )

    if (showDialog) {
        FrequencyInputDialog(
            initialValue = currentFrequency,
            onDismiss = { showDialog = false },
            onConfirm = { newFreq ->
                onFrequencyConfirmed(newFreq)
                showDialog = false
            }
        )
    }
}

@Composable
private fun FrequencyInputDialog(
    initialValue: String,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var text by remember { mutableStateOf(initialValue) }
    val value = text.toDoubleOrNull()
    val isValid = value != null && value in 87.5..108.0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ввести частоту") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { newValue ->
                    val filtered = newValue.filterIndexed { index, c ->
                        c.isDigit() || (c == '.' && !newValue.take(index).contains('.'))
                    }
                    val parts = filtered.split(".")
                    text = when (parts.size) {
                        1 -> parts[0].take(3)
                        2 -> parts[0].take(3) + "." + parts[1].take(2)
                        else -> filtered
                    }
                },
                label = { Text("87.5 – 108.0, шаг 0.05") },
                isError = text.isNotEmpty() && !isValid,
                supportingText = {
                    if (text.isNotEmpty() && !isValid) {
                        Text("Введите значение от 87.5 до 108.0 с шагом 0.05")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    value?.let {
                        val rounded = (it * 20).roundToInt() / 20.0
                        onConfirm(rounded)
                    }
                },
                enabled = isValid
            ) {
                Text("Настроить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}