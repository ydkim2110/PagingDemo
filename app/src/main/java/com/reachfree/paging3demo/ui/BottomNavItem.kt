package com.reachfree.paging3demo.ui

import android.util.Log.i
import androidx.compose.ui.res.stringResource
import com.reachfree.paging3demo.R
import com.reachfree.paging3demo.Screen

const val HOME = "HOME"
const val BOOKMARK = "BOOKMARK"

sealed class BottomNavItem(
    val title: Int,
    val icon: Int,
    val route: String
) {
    object Home : BottomNavItem(R.string.bottom_home_title, R.drawable.ic_error, Screen.Home.route)
    object Detail: BottomNavItem(R.string.bottom_detail_title, R.drawable.ic_error, Screen.Detail.route)
    object Bookmark : BottomNavItem(R.string.bottom_bookmark_title, R.drawable.ic_error, Screen.Bookmark.route)
}
