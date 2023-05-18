/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

/*
 * Githubリポジトリの検索、取得を担うViewModel
 */
class RepositorySearchViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _searchResults = MutableLiveData<List<RepositoryItem>>()
    val searchResults: LiveData<List<RepositoryItem>> = _searchResults

    fun performSearch(keyword: String) {
        _isLoading.value = true

        viewModelScope.launch {
            val results = withContext(Dispatchers.IO) { searchRepositories(keyword) }
            _searchResults.value = results

            _isLoading.value = false
        }
    }

    private fun searchRepositories(inputText: String): List<RepositoryItem> = runBlocking {
        val client = HttpClient(Android)
        try {
            val response: HttpResponse = client.get("https://api.github.com/search/repositories") {
                header("Accept", "application/vnd.github.v3+json")
                parameter("q", inputText)
            }

            if (response.status.isSuccess()) {
                val jsonBody = JSONObject(response.receive<String>())
                return@runBlocking processJsonResponse(jsonBody)
            }

        } catch (e: IOException) {
            ErrorHandler.handleIOException(e)
        } catch (e: JSONException) {
            ErrorHandler.handleJSONException(e)
        } catch (e: Exception) {
            ErrorHandler.handleException(e)
        }

        return@runBlocking emptyList()
    }

    private fun processJsonResponse(jsonBody: JSONObject): List<RepositoryItem> {
        val jsonItems = jsonBody.optJSONArray("items") ?: return emptyList()
        val items = mutableListOf<RepositoryItem>()

        for (i in 0 until jsonItems.length()) {
            val jsonItem = jsonItems.optJSONObject(i) ?: continue
            val item = createItemFromJson(jsonItem)
            items.add(item)
        }

        lastSearchDate = Date()

        return items
    }

    private fun createItemFromJson(jsonItem: JSONObject): RepositoryItem {
        val name = if(jsonItem.isNull("full_name")){"-no name data-"} else {jsonItem.optString("full_name")}
        val ownerIconUrl = jsonItem.optJSONObject("owner")?.optString("avatar_url") ?: ""
        val language = if (jsonItem.isNull("language")){"-no language data-"} else {jsonItem.optString("language")}
        val stargazersCount = jsonItem.optLong("stargazers_count")
        val watchersCount = jsonItem.optLong("watchers_count")
        val forksCount = jsonItem.optLong("forks_count")
        val openIssuesCount = jsonItem.optLong("open_issues_count")

        return RepositoryItem(
            name = name,
            ownerIconUrl = ownerIconUrl,
            language = language,
            stargazersCount = stargazersCount,
            watchersCount = watchersCount,
            forksCount = forksCount,
            openIssuesCount = openIssuesCount
        )
    }
}