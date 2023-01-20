package com.reachfree.paging3demo

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.compose.LazyPagingItems
import com.reachfree.paging3demo.data.network.Job
import com.reachfree.paging3demo.ui.BookmarkScreen
import com.reachfree.paging3demo.ui.HomeScreen
import com.reachfree.paging3demo.ui.MainViewModel

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    viewModel: MainViewModel,
    usersList: LazyPagingItems<Job>
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(
            route = Screen.Home.route
        ) {
            HomeScreen(usersList = usersList) { seq ->
                navController.navigate(route = "detail_screen/$seq")
            }
        }
        composable(
            route = Screen.Bookmark.route
        ) {
            BookmarkScreen()
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument(DETAIL_ARGUMENTS_KEY) {
                type = NavType.StringType
            })
        ) {
            DetailScreen(
                navController = navController,
                seq = it.arguments?.getString(DETAIL_ARGUMENTS_KEY),
                viewModel = viewModel
            )
            BackHandler {
                navController.popBackStack()
            }
        }
    }
}