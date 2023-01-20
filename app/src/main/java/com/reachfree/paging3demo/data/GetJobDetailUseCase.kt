package com.reachfree.paging3demo.data

import com.reachfree.paging3demo.Resource
import com.reachfree.paging3demo.data.repository.UserRepository
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetJobDetailUseCase @Inject constructor(
    private val repo: UserRepository
) {

    operator fun invoke(seq: Int) = flow {
        try {
            val response = repo.crawlJobDetail(seq = seq)
            emit(Resource.Success(response))
        } catch (e: IOException) {
            emit(Resource.Error("IO Exception: ${e.message}"))
        }
    }

}