package com.reachfree.paging3demo

const val DETAIL_ARGUMENTS_KEY = "seq"

sealed class Screen(val route: String) {
    object Home: Screen("home_screen")
    object Bookmark: Screen("bookmark_screen")
    object Detail: Screen("detail_screen/{$DETAIL_ARGUMENTS_KEY}")
}
