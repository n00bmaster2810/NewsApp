package com.example.newsapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.user.User
import com.example.newsapp.data.user.UserDatabase
import com.example.newsapp.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserViewModel(application: Application, var repository: UserRepository) :
    AndroidViewModel(application) {

    var users: List<User> = listOf()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getUser()
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    fun deleteUser() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUser()
            getUser()
        }
    }

    private suspend fun getUser(){
        viewModelScope.launch(Dispatchers.IO) {
            users = repository.getUser()
        }
    }

    fun checkUserIsLoggedInOrNot(): Boolean {
        return (users.isNotEmpty())
    }

}