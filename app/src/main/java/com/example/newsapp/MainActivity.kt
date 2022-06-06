package com.example.newsapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.newsapp.data.user.UserDatabase
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.repository.UserRepository
import com.example.newsapp.util.Constants
import com.example.newsapp.viewmodels.NewsViewModel
import com.example.newsapp.viewmodels.NewsViewModelProviderFactory
import com.example.newsapp.viewmodels.UserViewModel
import com.example.newsapp.viewmodels.UserViewModelFactory
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    //global variables
    lateinit var userViewModel: UserViewModel
    lateinit var newsViewModel: NewsViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialising the navController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.navController

        //checking to make sure whether internet is connected or not
        if (!Constants.hasInternetConnection(this)) {
            Toast.makeText(this, getString(R.string.networkError), Toast.LENGTH_SHORT).show()
        }

        //initialising the viewModels
        initUserViewModel()
        initNewsViewModel()

        checkIfUserIsLoggedIn()

        val appBarConfiguration = AppBarConfiguration
            .Builder(
                R.id.newsFragment,
                R.id.homeFragment
            )
            .build()
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    private fun checkIfUserIsLoggedIn() {
        if (userViewModel.checkUserIsLoggedInOrNot()) {
            navController.popBackStack(R.id.homeFragment, true)
            navController.navigate(R.id.newsFragment)
        }
    }

    private fun initUserViewModel() {
        val userDao = UserDatabase.getDatabase(this).userDao()
        val userRepository = UserRepository(userDao)
        val userViewModelFactory = UserViewModelFactory(application, userRepository)
        userViewModel = ViewModelProvider(this, userViewModelFactory)[UserViewModel::class.java]
    }

    private fun initNewsViewModel() {
        val newsRepository = NewsRepository()
        val viewModelProviderFactory =
            NewsViewModelProviderFactory(application, newsRepository)
        newsViewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        )[NewsViewModel::class.java]
    }

    //for the navigation up button
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.fragment)
        return navController.navigateUp()
    }

}