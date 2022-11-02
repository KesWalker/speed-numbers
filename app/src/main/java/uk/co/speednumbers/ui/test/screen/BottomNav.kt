package uk.co.speednumbers.ui.test.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import uk.co.speednumbers.R
import java.nio.file.Files.size

@Composable
fun BottomTestBar(navController: NavController) {
    val menuItems = listOf(
        TestScreens.MatchScreen,
        TestScreens.WriteScreen,
        TestScreens.SpeakScreen
    )
    BottomNavigation(
        Modifier.height(64.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 8.dp,
        contentColor = MaterialTheme.colors.onSurface
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        menuItems.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = screen.icon),
                        screen.route,
                        modifier = Modifier.size(32.dp),
                    )
                },
                label = { Text(stringResource(screen.resourceId)) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

sealed class TestScreens(
    val route: String,
    @StringRes val resourceId: Int,
    @DrawableRes val icon: Int
) {
    object WriteScreen : TestScreens("write", R.string.write, R.drawable.ic_pencil)
    object SpeakScreen : TestScreens("speak", R.string.speak, R.drawable.ic_mic)
    object MatchScreen : TestScreens("match", R.string.match, R.drawable.ic_squares)
}