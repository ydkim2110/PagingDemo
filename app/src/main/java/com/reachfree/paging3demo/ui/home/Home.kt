package com.reachfree.paging3demo.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.reachfree.paging3demo.ui.HomeScreen

enum class HomeTabs(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    HOME("Home", Icons.Rounded.Home, "home/list"),
    BOOKMARK("Bookmark", Icons.Rounded.Bookmark, "home/bookmark")
}

fun NavGraphBuilder.addHomeGraph(
    navController: NavHostController,
    navToJobDetail: () -> Unit,
    modifier: Modifier = Modifier
) {
    composable(
        route = HomeTabs.HOME.route
    ) {

    }
}