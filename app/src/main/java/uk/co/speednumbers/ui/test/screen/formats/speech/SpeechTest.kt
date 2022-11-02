package uk.co.speednumbers.ui.test

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.correctGreen
import com.example.compose.incorrectRed
import com.example.compose.nearlyOrange
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import uk.co.speednumbers.BuildConfig
import uk.co.speednumbers.R
import uk.co.speednumbers.ui.test.screen.formats.speech.SpeechViewModel
import uk.co.speednumbers.util.translateNum

@ExperimentalPermissionsApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SpeechTest(testViewModel: TestViewModel, speechViewModel: SpeechViewModel = viewModel()) {
    val permission = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    val context = LocalContext.current
    val language = testViewModel.languageSelected.value.language
    val animationSize = ((speechViewModel.soundWaves.value / 10) * 96).coerceAtLeast(16f)
    val showAnswer =
        testViewModel.answerState.value == AnswerState.INCORRECT || testViewModel.answerState.value == AnswerState.CLOSE_ANSWER

    val btnColor = when (testViewModel.answerState.value) {
        AnswerState.CORRECT -> MaterialTheme.colors.correctGreen
        AnswerState.ANSWERING -> MaterialTheme.colors.primary
        AnswerState.CLOSE_ANSWER -> MaterialTheme.colors.nearlyOrange
        AnswerState.INCORRECT -> MaterialTheme.colors.incorrectRed
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Say number in ${testViewModel.languageSelected.value.displayLanguage}:",
            Modifier.padding(8.dp)
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                when {
                    showAnswer -> testViewModel.nextNumber(null)
                    permission.status is PermissionStatus.Granted ->
                        startSpeechToText(testViewModel, speechViewModel, language, context)
                    permission.status is PermissionStatus.Denied -> permission.launchPermissionRequest()
                }
            },
            enabled = !testViewModel.inValidMaxAndMin(),
            colors = ButtonDefaults.buttonColors(backgroundColor = btnColor),
            modifier = Modifier
                .animateContentSize()
                .then(
                    if (!showAnswer) Modifier
                        .height(96.dp)
                        .width(96.dp)
                    else Modifier.wrapContentWidth()
                )
        ) {
            if (speechViewModel.isListening.value) {
                Icon(
                    painterResource(id = R.drawable.ic_circles),
                    null,
                    Modifier
                        .height(animationSize.dp)
                        .width(animationSize.dp),
                    tint = MaterialTheme.colors.onPrimary
                )
            } else if (showAnswer) {
                Text(
                    "NEXT NUMBER", fontSize = 16.sp, modifier = Modifier
                        .padding(8.dp)
                )
            } else {
                Icon(
                    painterResource(id = R.drawable.ic_mic),
                    null,
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }

        AnimatedVisibility(visible = speechViewModel.isError.value) {
            Text(text = speechViewModel.errorMessage.value, Modifier.padding(16.dp))
        }

    }
}

@OptIn(ExperimentalComposeUiApi::class)
fun startSpeechToText(
    testViewModel: TestViewModel,
    speechViewModel: SpeechViewModel,
    languageCode: String,
    context: Context,
) {
    speechViewModel.isListening.value = true
    speechViewModel.isError.value = false

    val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        .putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        .putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
        .putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, BuildConfig.APPLICATION_ID)

    speechRecognizer.setRecognitionListener(object : RecognitionListener {
        override fun onReadyForSpeech(bundle: Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(v: Float) {
            speechViewModel.soundWaves.value = v.coerceAtLeast(0f)
        }

        override fun onBufferReceived(bytes: ByteArray?) {}
        override fun onPartialResults(bundle: Bundle) {}
        override fun onEvent(i: Int, bundle: Bundle?) {}
        override fun onEndOfSpeech() {
            speechViewModel.isListening.value = false
            // changing the color of your mic icon to
            // gray to indicate it is not listening or do something you want
        }

        override fun onError(i: Int) {
            speechViewModel.speechError(i)
        }

        override fun onResults(bundle: Bundle) {
            speechViewModel.isListening.value = false
            val result = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (result != null) {
                Log.d("kesD", "onResults: results: $result")
                var res = result[0]
                res.toLongOrNull()
                    ?.let { res = translateNum(it, testViewModel.languageSelected.value) }
                testViewModel.usersInput.value = res
                testViewModel.markSpokenAnswer()
            }
        }
    })
    speechRecognizer.startListening(speechRecognizerIntent)
}