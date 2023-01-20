package com.reachfree.paging3demo.data.network

import com.reachfree.paging3demo.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.jsoup.Connection
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {

    @GET("user")
    suspend fun getUsers(@Query("page") page: Int, @Query("limit") limit: Int): UsersResponse

    @GET("brd/m_96/list.do")
    suspend fun getJobList(@Query("page") page: Int): JobsResponse

    @GET("brd/m_96/list.do")
    suspend fun getJobListPageNumber(@Query("page") page: Int): ResponseBody

    @GET("brd/m_96/view.do")
    suspend fun getJobDetail(@Query("seq") seq: Int): Call<ResponseBody>

    companion object {
        private const val BASE_URL = "https://dummyapi.io/data/v1/"
        private const val BASE_URL_JOB = "https://www.kofia.or.kr/"

        operator fun invoke(): UserApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL_JOB)
                .client(getRetrofitClient())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(UserApi::class.java)
        }

        private fun getRetrofitClient(): OkHttpClient {
            return OkHttpClient.Builder()
//                .addInterceptor { chain ->
//                    chain.proceed(chain.request().newBuilder().also {
//                        it.addHeader("Accept", "application/json")
//                        it.addHeader("app-id", "62cceaafb592b449c3aad899")
//                    }.build())
//                }
                .also { client ->
                    if (BuildConfig.DEBUG) {
                        val logging = HttpLoggingInterceptor()
                        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                        client.addInterceptor(logging)
                    }
                }.build()
        }
    }

}