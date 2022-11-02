package uk.co.speednumbers.ui.test

import androidx.compose.animation.Animatable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.correctGreen
import com.example.compose.incorrectRed
import com.example.compose.onCorrectGreen
import com.example.compose.onIncorrectRed
import uk.co.speednumbers.ui.test.screen.formats.match.MatchViewModel

@OptIn(
    ExperimentalComposeUiApi::class,
    androidx.compose.foundation.ExperimentalFoundationApi::class
)
@Composable
fun MatchTest(testViewModel: TestViewModel, matchViewModel: MatchViewModel = viewModel()) {
    val options = testViewModel.options.value
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if(options.size >= 4){
            MatchGrid(options,testViewModel)
        }else{
            Text("Not enough range, please increase the maximum above ${testViewModel.maximum.value}.", Modifier.padding(32.dp), textAlign = TextAlign.Center)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun MatchGrid(options: List<String>, testViewModel: TestViewModel) {
    LazyVerticalGrid(cells = GridCells.Fixed(2), userScrollEnabled = false) {
        items(options) {
            MatchGridItem(
                it,
                testViewModel.usersInput.value,
                testViewModel.answer.value,
                testViewModel.answerState.value != AnswerState.ANSWERING,
                !testViewModel.inValidMaxAndMin(),
            ) {
                if (testViewModel.answerState.value == AnswerState.ANSWERING) {
                    testViewModel.usersInput.value = it
                    testViewModel.markMatchAnswer()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MatchGridItem(
    option: String,
    userSelection: String,
    correctSelection: String,
    resultTime: Boolean,
    validMinAndMax: Boolean,
    onClick: () -> Unit
) {
    val correct = resultTime && option == correctSelection
    val incorrect = resultTime && userSelection == option && option != correctSelection
    val animate = correct || incorrect

    val color = when {
        correct -> MaterialTheme.colors.correctGreen
        incorrect -> MaterialTheme.colors.incorrectRed
        else -> MaterialTheme.colors.surface
    }
    val onColor = when {
        correct -> MaterialTheme.colors.onCorrectGreen
        incorrect -> MaterialTheme.colors.onIncorrectRed
        else -> MaterialTheme.colors.onSurface
    }

    val defaultColor = MaterialTheme.colors.surface

    val animatedColor = remember { Animatable(defaultColor) }
    LaunchedEffect(animate) {
        animatedColor.animateTo(color)
    }

    val textSize = when(option.length){
        in 0..20 -> 20
        in 20..40 -> 18
        in 40..60 -> 16
        else -> 14
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = animatedColor.value,
        elevation = 4.dp,
        contentColor = onColor,
        modifier = Modifier
            .padding(16.dp).defaultMinSize(minHeight = 128.dp),
        onClick = onClick,
        enabled = validMinAndMax
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = option, fontSize = textSize.sp, textAlign = TextAlign.Center)
        }
    }
}