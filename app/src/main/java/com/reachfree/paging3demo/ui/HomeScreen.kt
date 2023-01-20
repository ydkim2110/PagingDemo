package com.reachfree.paging3demo.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.gestures.OverScrollConfiguration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import com.reachfree.paging3demo.BannersAd
import com.reachfree.paging3demo.data.network.Job

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    usersList: LazyPagingItems<Job>,
    userCardClicked: (seq: String) -> Unit
) {
    CompositionLocalProvider(
        LocalOverScrollConfiguration provides OverScrollConfiguration(
            drawPadding = PaddingValues(vertical = 56.dp)
        ),

        content = {
            UserList(usersList = usersList) { userCardClicked(it) }
        }
    )
}