package uk.co.speednumbers.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.SpeedNumbersTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import uk.co.speednumbers.R
import uk.co.speednumbers.ui.languages.LanguageScreen
import uk.co.speednumbers.ui.languages.LanguagesViewModel
import uk.co.speednumbers.ui.settings.SettingsScreen
import uk.co.speednumbers.ui.test.TestScreen
import uk.co.speednumbers.ui.test.TestViewModel
import uk.co.speednumbers.ui.test.TestViewModel.Companion.SELECTED_LANGUAGE_YET_KEY
import java.security.AccessController.getContext

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalComposeUiApi::class)
    val testViewModel: TestViewModel by viewModels()

    @OptIn(
        ExperimentalComposeUiApi::class,
        ExperimentalPermissionsApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_SpeedNumbers)
        super.onCreate(savedInstanceState)

        setTts()

        setContent {
            SpeedNumbersTheme {
                val navController = rememberNavController()
                val languagesViewModel: LanguagesViewModel by viewModels()
                NavHost(navController = navController, startDestination = getFirstDest()) {
                    composable("test") {
                        MainScreen(
                            navController = navController,
                            testViewModel = testViewModel
                        )
                    }
                    composable("languages") {
                        LanguageScreen(
                            navController = navController,
                            testViewModel = testViewModel,
                            languagesViewModel = languagesViewModel
                        )
                    }
                    composable("settings") {
                        SettingsScreen(
                            navController = navController,
                            testViewModel = testViewModel
                        )
                    }
                }
                SystemColours()
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    private fun getFirstDest(): String {
        return if (testViewModel.prefs.getBoolean(SELECTED_LANGUAGE_YET_KEY, false)) {
            "test"
        } else {
            testViewModel.prefs.edit().putBoolean(SELECTED_LANGUAGE_YET_KEY, true).apply()
            "languages"
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    private fun setTts() {
        var tts: TextToSpeech? = null
        tts = TextToSpeech(applicationContext) {
            tts?.let {
                testViewModel.setTts(it)
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onDestroy() {
        testViewModel.speaker?.stop()
        testViewModel.speaker?.shutdown()
        super.onDestroy()
    }
}

@ExperimentalPermissionsApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(navController: NavHostController, testViewModel: TestViewModel) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        TestScreen(viewModel = testViewModel, navController)
    }
}

@Composable
fun SystemColours() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight

    val color = MaterialTheme.colors.surface

    val elevationOverlay = LocalElevationOverlay.current
    val absoluteElevation = LocalAbsoluteElevation.current + 8.dp
    val systemBarColors = if (color == MaterialTheme.colors.surface && elevationOverlay != null) {
        elevationOverlay.apply(color, absoluteElevation)
    } else {
        color
    }

    SideEffect {
        systemUiController.setStatusBarColor(
            color = systemBarColors,
            darkIcons = useDarkIcons
        )
        systemUiController.setNavigationBarColor(
            color = systemBarColors
        )
    }
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