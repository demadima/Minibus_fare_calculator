package com.minibusfare.calculator.ui.fare.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minibusfare.calculator.ui.theme.MinibusFareCalculatorTheme

@Composable
fun PresetAmountGrid(
    presets: List<Int>,
    currentAmountPaid: Int,
    onPresetSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    buttonHeight: Dp = 36.dp
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        presets.chunked(5).forEach { rowPresets ->
            PresetRow(
                rowPresets = rowPresets,
                currentAmountPaid = currentAmountPaid,
                onPresetSelected = onPresetSelected,
                buttonHeight = buttonHeight
            )
        }
    }
}

@Composable
private fun PresetRow(
    rowPresets: List<Int>,
    currentAmountPaid: Int,
    onPresetSelected: (Int) -> Unit,
    buttonHeight: Dp
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        rowPresets.forEach { amount ->
            val isSelected = amount == currentAmountPaid
            val containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
            val contentColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurface
            }
            val borderColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
            }

            OutlinedButton(
                onClick = { onPresetSelected(amount) },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(0.dp),
                border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = containerColor,
                    contentColor = contentColor
                )
            ) {
                Text(
                    text = "$amount",
                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
        repeat(5 - rowPresets.size) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(name = "Preset Amount Grid", showBackground = true)
@Composable
fun PresetAmountGridPreview() {
    MinibusFareCalculatorTheme {
        PresetAmountGrid(
            presets = listOf(40, 50, 60, 70, 80, 90, 100, 120, 140, 150, 160, 200, 500, 1000, 2000),
            currentAmountPaid = 100,
            onPresetSelected = {},
            modifier = Modifier.padding(12.dp)
        )
    }
}
