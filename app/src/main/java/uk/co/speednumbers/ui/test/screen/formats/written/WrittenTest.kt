package uk.co.speednumbers.ui.test

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.correctGreen
import com.example.compose.incorrectRed
import com.example.compose.nearlyOrange

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WrittenTest(viewModel: TestViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        val showAnswer =
            viewModel.answerState.value == AnswerState.INCORRECT || viewModel.answerState.value == AnswerState.CLOSE_ANSWER
        val keyboard = LocalSoftwareKeyboardController.current
        UserAnswerInput(viewModel, keyboard)
        Button(
            onClick = { viewModel.answerButtonClick(keyboard) },
            enabled = !viewModel.inValidMaxAndMin(),
            modifier = Modifier.padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(Dp(32f)))
            Text(
                if (showAnswer) "NEXT NUMBER" else "CHECK ANSWER",
                fontSize = 16.sp
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserAnswerInput(
    viewModel: TestViewModel,
    keyboard: SoftwareKeyboardController?,
) {
    val outlineColor = when (viewModel.answerState.value) {
        AnswerState.CORRECT -> MaterialTheme.colors.correctGreen
        AnswerState.ANSWERING -> MaterialTheme.colors.primary
        AnswerState.CLOSE_ANSWER -> MaterialTheme.colors.nearlyOrange
        AnswerState.INCORRECT -> MaterialTheme.colors.incorrectRed
    }
    OutlinedTextField(
        value = viewModel.usersInput.value,
        onValueChange = { viewModel.usersInput.value = it },
        label = { Text("Write number in ${viewModel.languageSelected.value.displayLanguage}") },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            capitalization = KeyboardCapitalization.None
        ),
        keyboardActions = KeyboardActions(
            onDone = { viewModel.markAnswer(keyboard) }
        ),
        readOnly = viewModel.answerState.value != AnswerState.ANSWERING,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = outlineColor,
            focusedLabelColor = outlineColor
        )
    )
}