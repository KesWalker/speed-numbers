package uk.co.speednumbers.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.glide.rememberGlidePainter
import uk.co.speednumbers.R
import uk.co.speednumbers.ui.test.TestViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingsScreen(
    navController: NavController, testViewModel: TestViewModel,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
            TopAppBar(
                title = { Text("More") },
                elevation = 8.dp,
                backgroundColor = MaterialTheme.colors.surface,
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "back")
                    }
                }
            )
        }) {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            SpeedLanguagesAd()
            Spacer(Modifier.height(16.dp))
            TitleRow("Settings")
            SoundEffectsSwitch(testViewModel = testViewModel)
            ReportIssue()
        }
    }
}

@Composable
fun TitleRow(title: String) {
    Text(
        title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(start = 8.dp)
    )
}

@Composable
fun ReportIssue() {
    val context = LocalContext.current
    Row(
        Modifier
            .clickable {
                context.startActivity(
                    Intent(
                        Intent.ACTION_SENDTO,
                        Uri.parse("mailto:kes@speedlanguages.co.uk")
                    )
                )
            }
            .padding(16.dp)
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Report issue")
    }
}

@Composable
fun PrivacyPolicy(){

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SpeedLanguagesAd() {
    val context = LocalContext.current
    Card(onClick = {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/dev?id=6651481676089454066")
            )
        )
    }, shape = RoundedCornerShape(16.dp), modifier = Modifier.padding(16.dp), elevation = 8.dp) {
        Column {
            Image(painter = rememberGlidePainter(
                "https://storage.googleapis.com/memes-bucket123/other/speedlangad.png",
                requestBuilder = { placeholder(R.drawable.speed_languages) }
            ), null)
            Column(Modifier.padding(16.dp)) {
                Text("Explore our other apps", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Lessons, memes, multiplayer & an array of tools to help you learn a new language.",
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Download a Speed Language app.",
                    color = MaterialTheme.colors.primary.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SoundEffectsSwitch(testViewModel: TestViewModel) {
    Row(
        Modifier
            .clickable { testViewModel.setSoundEffects() }
            .padding(16.dp)
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Sound effects")
        Spacer(modifier = Modifier.weight(1f))
        Checkbox(
            testViewModel.soundEffects.value,
            onCheckedChange = { testViewModel.setSoundEffects(it) })
    }
}
