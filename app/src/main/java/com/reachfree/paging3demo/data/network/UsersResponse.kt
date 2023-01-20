package com.reachfree.paging3demo.data.network

import com.squareup.moshi.Json

data class UsersResponse(
    @field:Json(name = "data")
    val users: List<User>,
    @field:Json(name = "limit")
    val limit: Int,
    @field:Json(name = "page")
    val page: Int,
    @field:Json(name = "total")
    val total: Int
)
