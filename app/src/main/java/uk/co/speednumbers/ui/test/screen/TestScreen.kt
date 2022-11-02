package uk.co.speednumbers.ui.test

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.SpeedNumbersTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import uk.co.speednumbers.ui.test.screen.BottomTestBar
import uk.co.speednumbers.ui.test.screen.MinAndMaxNumbers
import uk.co.speednumbers.ui.test.screen.TestScreens
import uk.co.speednumbers.ui.toolbar.TopTestBar
import java.text.DecimalFormat
import uk.co.speednumbers.R


@ExperimentalPermissionsApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TestScreen(viewModel: TestViewModel = viewModel(), parentNavController: NavController) {
    val navController = rememberNavController()
    Scaffold(
        topBar = { TopTestBar(viewModel, parentNavController) },
        bottomBar = { BottomTestBar(navController) }
    ) {
        TestBody(viewModel = viewModel, navController)
    }
}

@ExperimentalPermissionsApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TestBody(viewModel: TestViewModel, navController: NavHostController) {
    val showAnswer =
        (viewModel.answerState.value == AnswerState.INCORRECT || viewModel.answerState.value == AnswerState.CLOSE_ANSWER)
                && viewModel.currentFormat.value != TestScreens.MatchScreen.route

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    navBackStackEntry?.destination?.route?.let {
        viewModel.currentFormat.value = it
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.weight(0.1f))
        MinAndMaxNumbers(viewModel = viewModel)
        Spacer(modifier = Modifier.weight(1f))
        NumberToTranslate(
            number = viewModel.numberToTranslate.value.toString(),
            viewModel.maximum.value
        )

        NavHost(navController = navController, startDestination = TestScreens.WriteScreen.route) {
            composable(TestScreens.WriteScreen.route) { WrittenTest(viewModel = viewModel) }
            composable(TestScreens.SpeakScreen.route) { SpeechTest(testViewModel = viewModel) }
            composable(TestScreens.MatchScreen.route) { MatchTest(testViewModel = viewModel) }
        }

        Spacer(modifier = Modifier.weight(0.15f))
        AnimatedVisibility(visible = showAnswer) { IncorrectMessage(viewModel) }
        Spacer(modifier = Modifier.weight(2f))
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun IncorrectMessage(viewModel: TestViewModel) {
    Box(modifier = Modifier.wrapContentHeight()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                if (viewModel.answerState.value == AnswerState.CLOSE_ANSWER) "Nearly! Correct answer:" else "Correct answer:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.clickable(onClick = { viewModel.sayWords(viewModel.answer.value) }),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    viewModel.answer.value,
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_speaker),
                    null,
                    tint = MaterialTheme.colors.primary
                )
            }
        }
    }
}

@Composable
fun NumberToTranslate(number: String, max: String) {
    val style = when (max.length) {
        in 0..3 -> MaterialTheme.typography.h1
        else -> MaterialTheme.typography.h2
    }
    Text(DecimalFormat("#,###.##").format(number.toLong()), style = style)
}


@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalPermissionsApi::class
)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SpeedNumbersTheme {
        TestScreen(parentNavController = rememberNavController())
    }
}