package com.reachfree.paging3demo.data.network

import com.squareup.moshi.Json

data class JobsResponse(
    @field:Json(name = "data")
    val jobs: List<Job>,
    @field:Json(name = "page")
    val page: Int
)
