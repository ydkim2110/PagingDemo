package com.reachfree.paging3demo.data.repository

import android.util.Log
import com.reachfree.paging3demo.data.network.Job
import com.reachfree.paging3demo.data.network.JobsResponse
import com.reachfree.paging3demo.data.network.UserApi
import com.reachfree.paging3demo.data.network.UsersResponse
import kotlinx.coroutines.delay
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Call
import java.io.IOException

class UserRepositoryImpl(
    private val api: UserApi
) : UserRepository {

    var error = 0

    override suspend fun getUsers(page: Int, limit: Int): UsersResponse {
        error++
        if (error == 4)
            throw IOException("Some error occurred")

        return api.getUsers(page, limit)
    }

    override suspend fun getJobList(page: Int): JobsResponse {
        return  api.getJobList(page)
    }

    override suspend fun getJobListNumber(page: Int): ResponseBody {
        return api.getJobListPageNumber(page)
    }

    override suspend fun getJobDetail(seq: Int): Call<ResponseBody> {
        return api.getJobDetail(seq)
    }

    override suspend fun crawlJobList(page: Int): List<Job> {
        val result = Jsoup.connect("https://www.kofia.or.kr/brd/m_96/list.do?page=$page").execute()

        val doc: Document = Jsoup.parse(result.body())

        val tableList = doc.select("div#contentArea2 table tbody tr")

        val jobs = mutableListOf<Job>()
        var seq = ""
        tableList.forEach { row ->
            val cellList = row.select("td")

            cellList.forEachIndexed { index, td ->
                if (index == 2) {
                    val links = td.select("a[href]")
                    val seqATag = links.attr("href")
                    val firstIndexOf = seqATag.indexOf("?")
                    val lastIndexOf = seqATag.indexOf("&")

                    val splitText = seqATag.substring(firstIndexOf + 1, lastIndexOf)
                    val firstIndexOfNumber = splitText.indexOf("=")
                    val result = splitText.substring(firstIndexOfNumber + 1)

                    seq = result
                }
            }

            val number = cellList[0].text()
            val name = cellList[1].text()
            val title = cellList[2].text() + cellList[2].select("span").select("a").text()
            val file = cellList[3].text()
            val regDate = cellList[4].text()

            jobs.add(Job(number, name, title, file, regDate, seq))
        }

        return jobs
    }

    override suspend fun crawlJobDetail(seq: Int): Map<String, String> {
        val result = Jsoup.connect("https://www.kofia.or.kr/brd/m_96/view.do?seq=$seq").execute()

        val doc: Document = Jsoup.parse(result.body())

        val tableList = doc.select("div#contentArea1 table tbody tr")

        val jobs = mutableListOf<Job>()
        val jobDetailMap = mutableMapOf<String, String>()

        tableList.forEachIndexed { index, row ->
            when (tableList.size) {
                6 -> {
                    if (index in 0..3) {
                        val titleList = row.select("th")

                        titleList.forEachIndexed { index, element ->
                            jobDetailMap[element.text()] = row.select("td")[index].text()
                        }
                    } else if (index == 5) {
                        jobDetailMap["내용"] = row.select("td").select("div").html()
                    }
                }
                7 -> {
                    if (index in 0..3) {
                        val titleList = row.select("th")

                        titleList.forEachIndexed { index, element ->
                            jobDetailMap[element.text()] = row.select("td")[index].text()
                        }
                    } else if (index == 6) {
                        jobDetailMap["내용"] = row.select("td").select("div").html()
                    }
                }
                8 -> {
                    if (index in 0..3) {
                        val titleList = row.select("th")

                        titleList.forEachIndexed { index, element ->
                            jobDetailMap[element.text()] = row.select("td")[index].text()
                        }
                    } else if (index == 7) {
                        jobDetailMap["내용"] = row.select("td").select("div").html()
                    }
                }
                9 -> {
                    if (index in 0..3) {
                        val titleList = row.select("th")

                        titleList.forEachIndexed { index, element ->
                            jobDetailMap[element.text()] = row.select("td")[index].text()
                        }
                    } else if (index == 8) {
                        jobDetailMap["내용"] = row.select("td").select("div").html()
                    }
                }
                10 -> {
                    if (index in 0..3) {
                        val titleList = row.select("th")

                        titleList.forEachIndexed { index, element ->
                            jobDetailMap[element.text()] = row.select("td")[index].text()
                        }
                    } else if (index == 9) {
                        jobDetailMap["내용"] = row.select("td").select("div").html()
                    }
                }
            }
        }

        return jobDetailMap
    }
}