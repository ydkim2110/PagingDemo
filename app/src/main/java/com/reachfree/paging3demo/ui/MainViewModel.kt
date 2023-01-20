package com.reachfree.paging3demo.ui

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.reachfree.paging3demo.Resource
import com.reachfree.paging3demo.data.GetJobDetailUseCase
import com.reachfree.paging3demo.data.UserDataSource
import com.reachfree.paging3demo.data.network.Job
import com.reachfree.paging3demo.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: UserRepository,
    private val jobDetailUseCase: GetJobDetailUseCase
) : ViewModel() {

//    val usersPager = Pager(
//        PagingConfig(pageSize = 10)
//    ) {
//        UserDataSource(repo)
//    }.flow.cachedIn(viewModelScope)

    private var _jobDetailState = mutableStateOf(JobDetailState())
    val jobDetailState: State<JobDetailState> = _jobDetailState

    private val _state = MutableStateFlow(mutableMapOf<String, String>())
    val state: StateFlow<Map<String, String>> get() = _state

    var detailResult: Map<String, String> by mutableStateOf(mapOf())

    val jobsPager = Pager(
        PagingConfig(pageSize = 10)
    ) {
        UserDataSource(repo)
    }.flow.cachedIn(viewModelScope)

    fun getJobDetail(seq: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = mutableMapOf()
            val result = repo.crawlJobDetail(seq)
            _state.value = result as MutableMap<String, String>
//            val result = repo.crawlJobDetail(seq)
//            detailResult = result
            jobDetailUseCase(seq = seq).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _jobDetailState.value = JobDetailState(
                            isLoading = true
                        )
                    }
                    is Resource.Success -> {
                        _jobDetailState.value = JobDetailState(
                            data = resource.data
                        )
                    }
                    is Resource.Error -> {
                        _jobDetailState.value = JobDetailState(
                            hasError = true,
                            errorMessage = resource.message
                        )
                    }
                }
            }
        }
    }

}

data class JobDetailState(
    val data: Map<String, String>? = null,
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val errorMessage: String? = null
)