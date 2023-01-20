package com.reachfree.paging3demo.data.network

import com.squareup.moshi.Json

data class Job(
    @field:Json(name = "number")
    val number: String,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "title")
    val title: String,
    @field:Json(name = "file")
    val file: String,
    @field:Json(name = "regDate")
    val regDate: String,
    @field:Json(name = "seq")
    val seq: String
)
