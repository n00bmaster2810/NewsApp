package com.example.newsapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.R
import com.example.newsapp.data.news.NewsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.util.Constants
import com.example.newsapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

// to interact with the repository and ultimately fetch the data and observe the changes
class NewsViewModel(
    application: Application,
    private val newsRepository: NewsRepository
) : AndroidViewModel(application) {

    private val _breakingNewsFlow: MutableStateFlow<Resource<NewsResponse>> =
        MutableStateFlow(Resource.Loading())
    val breakingNewsFlow: StateFlow<Resource<NewsResponse>> = _breakingNewsFlow

    private var breakingNewsPage = 1

    init {
        getBreakingNews("in")
    }

    private fun getBreakingNews(countryCode: String) =
        viewModelScope.launch {
            safeNewsCall(countryCode)
        }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeNewsCall(countryCode: String) {
        _breakingNewsFlow.value = (Resource.Loading())
        try {
            if (Constants.hasInternetConnection(getApplication())) {
                val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
                _breakingNewsFlow.value = (handleBreakingNewsResponse(response))
            } else {
                _breakingNewsFlow.value =
                    (Resource.Error(getApplication<Application>().getString(R.string.networkError)))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> _breakingNewsFlow.value = (
                        Resource.Error(
                            getApplication<Application>().getString(
                                R.string.apiFailure
                            )
                        )
                        )
                else -> _breakingNewsFlow.value = (
                        Resource.Error(
                            getApplication<Application>().getString(
                                R.string.retrofitError
                            )
                        )
                        )
            }
        }
    }
}