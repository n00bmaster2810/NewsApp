package com.example.newsapp.repository

import androidx.lifecycle.LiveData
import com.example.newsapp.data.user.User
import com.example.newsapp.data.user.UserDao

// to interact with the functionalities of db
class UserRepository(private val userDao: UserDao) {

    suspend fun addUser(user: User) {
        userDao.addUser(user)
    }

    suspend fun deleteUser() {
        userDao.deleteUser()
    }

    suspend fun getUser(): List<User> {
        return userDao.getCurrentUser()
    }

}