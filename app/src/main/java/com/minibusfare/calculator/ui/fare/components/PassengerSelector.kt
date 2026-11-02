package com.minibusfare.calculator.ui.fare.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minibusfare.calculator.R
import com.minibusfare.calculator.ui.theme.MinibusFareCalculatorTheme

@Composable
fun PassengerSelector(
    title: String,
    pricePerPerson: Int,
    selectedCount: Int,
    onCountSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxCount: Int = 5,
    allowZero: Boolean = true,
    buttonHeight: Dp = 36.dp
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Text(
                text = stringResource(R.string.passenger_count_format, pricePerPerson * selectedCount, selectedCount),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val startCount = if (allowZero) 0 else 1
            for (count in startCount..maxCount) {
                val isSelected = count == selectedCount
                val buttonModifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)

                if (isSelected) {
                    Button(
                        onClick = { onCountSelected(count) },
                        modifier = buttonModifier,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = count.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                } else {
                    OutlinedButton(
                        onClick = { onCountSelected(count) },
                        modifier = buttonModifier,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = count.toString(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Passenger Selector Adult", showBackground = true)
@Composable
fun PassengerSelectorAdultPreview() {
    MinibusFareCalculatorTheme {
        PassengerSelector(
            title = stringResource(R.string.label_adults),
            pricePerPerson = 15,
            selectedCount = 2,
            onCountSelected = {},
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Preview(name = "Passenger Selector Child", showBackground = true)
@Composable
fun PassengerSelectorChildPreview() {
    MinibusFareCalculatorTheme {
        PassengerSelector(
            title = stringResource(R.string.label_children),
            pricePerPerson = 8,
            selectedCount = 1,
            onCountSelected = {},
            modifier = Modifier.padding(12.dp)
        )
    }
}
