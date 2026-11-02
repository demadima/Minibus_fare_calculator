package com.minibusfare.calculator.ui.fare.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minibusfare.calculator.R
import com.minibusfare.calculator.ui.theme.MinibusFareCalculatorTheme

@Composable
fun GivenAmountSection(
    amountPaidText: String,
    onAmountPaidChanged: (String) -> Unit,
    onClearPaidClicked: () -> Unit,
    modifier: Modifier = Modifier,
    fieldHeight: Dp = 52.dp
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = stringResource(R.string.given_amount_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = amountPaidText,
                onValueChange = onAmountPaidChanged,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = fieldHeight),
                placeholder = { Text("0 ₴", fontSize = 16.sp) },
                suffix = {
                    Text(
                        text = stringResource(R.string.currency_symbol),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(10.dp)
            )

            if (amountPaidText.isNotEmpty()) {
                OutlinedButton(
                    onClick = onClearPaidClicked,
                    modifier = Modifier.heightIn(min = fieldHeight),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.btn_clear),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Preview(name = "Given Amount Section Empty", showBackground = true)
@Composable
fun GivenAmountSectionEmptyPreview() {
    MinibusFareCalculatorTheme {
        GivenAmountSection(
            amountPaidText = "",
            onAmountPaidChanged = {},
            onClearPaidClicked = {},
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Preview(name = "Given Amount Section Filled", showBackground = true)
@Composable
fun GivenAmountSectionFilledPreview() {
    MinibusFareCalculatorTheme {
        GivenAmountSection(
            amountPaidText = "100",
            onAmountPaidChanged = {},
            onClearPaidClicked = {},
            modifier = Modifier.padding(12.dp)
        )
    }
}
