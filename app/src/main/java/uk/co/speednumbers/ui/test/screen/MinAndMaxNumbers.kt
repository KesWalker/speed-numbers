package uk.co.speednumbers.ui.test.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import uk.co.speednumbers.R
import uk.co.speednumbers.ui.test.TestViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MinAndMaxNumbers(viewModel: TestViewModel) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.weight(0.3f))
        OutlinedTextField(
            value = viewModel.minimum.value,
            onValueChange = { value -> if(value.length <= 10) viewModel.setMinimum(value) },
            label = { Text("Minimum") },
            modifier = Modifier
                .weight(1f)
                .focusRequester(FocusRequester())
                .onFocusChanged { },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            isError = viewModel.invalidMin()
        )
        IconButton(onClick = {
            viewModel.shuffle.value = !viewModel.shuffle.value
            if(!viewModel.shuffle.value){
                viewModel.numberToTranslate.value = viewModel.minimum.value.toLong()
            }
        }) {
            Icon(
                painter = painterResource(id = if (viewModel.shuffle.value) R.drawable.ic_shuffle else R.drawable.ic_right_arrow),
                contentDescription = "Shuffle arrow",
                Modifier.padding(top = 8.dp)
            )
        }
        OutlinedTextField(
            value = viewModel.maximum.value,
            onValueChange = { value -> if(value.length <= 10) viewModel.setMaximum(value) },
            label = { Text("Maximum") },
            modifier = Modifier
                .weight(1f)
                .onFocusChanged {
                    if (viewModel.minimum.value.isNotEmpty() && viewModel.maximum.value.isNotEmpty() && viewModel.minimum.value.toLong() > viewModel.maximum.value.toLong()) {
                        viewModel.minimum.value = viewModel.maximum.value
                    }
                },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            isError = viewModel.invalidMax()
        )
        Spacer(modifier = Modifier.weight(0.3f))
    }
}