package uk.co.speednumbers.ui.languages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.compose.SpeedNumbersTheme
import com.google.accompanist.glide.rememberGlidePainter
import uk.co.speednumbers.R
import uk.co.speednumbers.ui.test.TestViewModel
import uk.co.speednumbers.ui.toolbar.TopDefaultBar
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LanguageScreen(
    languagesViewModel: LanguagesViewModel,
    testViewModel: TestViewModel,
    navController: NavController
) {
    val languages = languagesViewModel.languages.value
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
            TopAppBar(
                title = { Text("Select a language") },
                elevation = 8.dp,
                backgroundColor = MaterialTheme.colors.surface,
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "back")
                    }
                }
            )
        }) {
        if (languages.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn {
                itemsIndexed(languages) { i, language ->
                    when (i) {
                        0 -> ListTitle("Popular")
                        languagesViewModel.popularLangs.size -> ListTitle("All (${languages.size - i})")
                    }
                    LanguageItem(language) {
                        testViewModel.setNewLanguage(language)
                        navController.navigate("test")
                    }
                }
            }

        }
    }
}

@Composable
fun ListTitle(title: String) {
    Text(text = title, Modifier.padding(8.dp))
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LanguageItem(language: Locale, onClick: () -> Unit) {
    val imgUrl =
        "https://raw.githubusercontent.com/KesWalker/PNG-Round-Country-Flags/main/${language.language}.png"
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick.invoke() }
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberGlidePainter(
                    imgUrl,
                    requestBuilder = { placeholder(R.drawable.ic_default_flag) }
                ),
                contentDescription = language.country,
                modifier = Modifier
                    .height(32.dp)
                    .width(32.dp)
            )
            Spacer(Modifier.width(16.dp))
            Text(text = language.displayLanguage, style = MaterialTheme.typography.h6)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SpeedNumbersTheme {
        LanguageItem(language = Locale.CANADA) {

        }
    }
}