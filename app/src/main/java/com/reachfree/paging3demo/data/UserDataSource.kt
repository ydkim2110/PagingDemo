package com.reachfree.paging3demo.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.reachfree.paging3demo.data.network.Job
import com.reachfree.paging3demo.data.network.User
import com.reachfree.paging3demo.data.repository.UserRepository
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class UserDataSource(
    private val repo: UserRepository
): PagingSource<Int, Job>() {

    override fun getRefreshKey(state: PagingState<Int, Job>): Int? {
        return state.anchorPosition?.let { position ->
            val page = state.closestPageToPosition(position)
            page?.prevKey?.minus(1) ?: page?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Job> {
        return try {
            val page = params.key ?: 1

            val response = withContext(Dispatchers.IO) {
                repo.crawlJobList(page)
            }

            //https://velog.io/@eoqkrskfk94/Paging-3-MVVM-Coroutine-Hilt-Flow%EB%A5%BC-%EC%82%AC%EC%9A%A9%ED%95%B4-RecyclerView-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0
            Log.d("UserDataSource", "RESULT: $response")

            LoadResult.Page(
                data = response,
                prevKey = null,
//                nextKey = if (response.jobs.isNotEmpty()) response.page + 1 else null
            nextKey = if (response.isNotEmpty()) page + 1 else null
            )

        } catch (e: Exception) {
            Log.d("DEBUG Error", "ERROR ${e}")
            LoadResult.Error(e)
        }
    }

//    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
//        return state.anchorPosition?.let { position ->
//            val page = state.closestPageToPosition(position)
//            page?.prevKey?.minus(1) ?: page?.nextKey?.plus(1)
//        }
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
//        return try {
//            val page = params.key ?: 1
//            val response = repo.getUsers(page, 10)
//            LoadResult.Page(
//                data = response.users,
//                prevKey = null,
//                nextKey = if (response.users.isNotEmpty()) response.page + 1 else null
//            )
//
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
}