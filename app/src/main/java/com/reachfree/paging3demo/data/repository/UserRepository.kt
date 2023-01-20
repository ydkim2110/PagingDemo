package com.reachfree.paging3demo.data.repository

import com.reachfree.paging3demo.data.network.Job
import com.reachfree.paging3demo.data.network.JobsResponse
import com.reachfree.paging3demo.data.network.UsersResponse
import okhttp3.ResponseBody
import retrofit2.Call

interface UserRepository {
    suspend fun getUsers(page: Int, limit: Int): UsersResponse
    suspend fun getJobList(page: Int): JobsResponse
    suspend fun getJobListNumber(page: Int): ResponseBody
    suspend fun getJobDetail(seq: Int): Call<ResponseBody>
    suspend fun crawlJobList(page: Int): List<Job>
    suspend fun crawlJobDetail(seq: Int): Map<String, String>
}