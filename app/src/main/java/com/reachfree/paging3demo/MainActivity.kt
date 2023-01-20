package com.reachfree.paging3demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.reachfree.paging3demo.data.network.Job
import com.reachfree.paging3demo.ui.BottomNavItem
import com.reachfree.paging3demo.ui.MainViewModel
import com.reachfree.paging3demo.ui.theme.Paging3DemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)


        MobileAds.initialize(this)

        setContent {
            val usersList: LazyPagingItems<Job> = viewModel.jobsPager.collectAsLazyPagingItems()

            Paging3DemoTheme {
                // A surface container using the 'background' color from the theme

                val bottomBarState = rememberSaveable { mutableStateOf(true) }
                val topBarState = rememberSaveable { mutableStateOf(true) }

                navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                when (navBackStackEntry?.destination?.route) {
                    Screen.Home.route -> {
                        bottomBarState.value = true
                        topBarState.value = true
                    }
                    Screen.Bookmark.route -> {
                        bottomBarState.value = true
                        topBarState.value = true
                    }
                    Screen.Detail.route -> {
                        bottomBarState.value = false
                        topBarState.value = false
                    }
                }

                com.google.accompanist.insets.ui.Scaffold(
                    topBar = {
                        Column() {
//                            TopBar(
//                                navController = navController,
//                                topBarState = topBarState
//                            )
                            TopBarBannersAd(
                                navController = navController,
                                topBarState = topBarState,
                                deviceCurrentWidth = LocalConfiguration.current.screenWidthDp
                            )
                        }
                     },
                    bottomBar = {
                        Column() {
                            BottomNavigation(
                                navController = navController,
                                bottomBarState = bottomBarState
                            )
                        }
                    }
                ) {
                    SetupNavGraph(
                        navController = navController,
                        viewModel = viewModel,
                        usersList = usersList
                    )
                }
            }
        }
    }
}

@Composable
fun TopBarBannersAd(
    navController: NavHostController,
    topBarState: MutableState<Boolean>,
    deviceCurrentWidth: Int
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    AnimatedVisibility(
        visible = topBarState.value,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it })
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                        context,
                        deviceCurrentWidth
                    )
                    adUnitId = context.getString(R.string.ad_id_banner)
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}

@Composable
fun TopBar(
    navController: NavHostController,
    topBarState: MutableState<Boolean>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    AnimatedVisibility(
        visible = topBarState.value,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it })
    ) {
        TopAppBar(
            title = {
                Text(text = stringResource(id = R.string.app_name))
            }
        )
    }
}

@Composable
fun BottomNavigation(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Bookmark
    )

    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        BottomNavigation {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            items.forEach { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = stringResource(id = item.title),
                            modifier = Modifier
                                .width(26.dp)
                                .height(26.dp)
                        )
                    },
                    label = { Text(
                        text = stringResource(id = item.title),
                        fontSize = 9.sp
                    )},
                    selected = currentRoute == item.route,
                    alwaysShowLabel = false,
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(it) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}