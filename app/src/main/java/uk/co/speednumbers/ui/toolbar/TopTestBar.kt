package uk.co.speednumbers.ui.toolbar

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.glide.rememberGlidePainter
import uk.co.speednumbers.R
import uk.co.speednumbers.ui.test.TestViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TopTestBar(testViewModel: TestViewModel, navController: NavController) {
    val languageSelected = testViewModel.languageSelected.value
    val imgUrl =
        "https://raw.githubusercontent.com/KesWalker/PNG-Round-Country-Flags/main/${languageSelected.language}.png"
    Log.d("kesD", "TopBar: imgurl: $imgUrl")
    Surface(
        color = MaterialTheme.colors.surface,
        elevation = 8.dp,
        contentColor = MaterialTheme.colors.onSurface
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.navigate("languages") }) {
                Image(
                    painter = rememberGlidePainter(
                        imgUrl,
                        requestBuilder = { placeholder(R.drawable.ic_default_flag) }
                    ),
                    contentDescription = languageSelected.country,
                    modifier = Modifier
                        .height(32.dp)
                        .width(32.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painterResource(id = R.drawable.ic_thunder),
                contentDescription = "Thunder bolt",
                modifier = Modifier
                    .height(20.dp)
                    .width(20.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
            )
            Text(text = "${testViewModel.streak.value}", style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {navController.navigate("settings") }) {
                Icon(Icons.Filled.MoreVert, "Settings")
            }
        }
    }
}