package com.example.newsapp.repository

import com.example.newsapp.api.RetrofitInstance

// to interact with the api made
class NewsRepository {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)
}
