package com.example.newsapp.data.news

import androidx.room.Entity
import java.io.Serializable


@Entity(
    tableName = "articles"
)
data class Article(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String,
    val urlToImage: String?
) : Serializable