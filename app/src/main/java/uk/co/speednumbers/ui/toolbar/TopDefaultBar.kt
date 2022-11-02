package uk.co.speednumbers.ui.toolbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TopDefaultBar(navController: NavController, title: String) {
    Surface(
        color = MaterialTheme.colors.surface,
        elevation = 8.dp,
        contentColor = MaterialTheme.colors.onSurface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.Default.ArrowBack, "back")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, style = MaterialTheme.typography.h5)
        }
    }
}