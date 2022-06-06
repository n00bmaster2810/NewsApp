package com.example.newsapp.ui.home

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.data.user.User
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.viewmodels.UserViewModel


class HomeFragment : Fragment() {

    private lateinit var mUserViewModel: UserViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        mUserViewModel = (activity as MainActivity).userViewModel

        // to login the user
        binding.loginBtn.setOnClickListener {
            login()
        }

    }

    //function for login the user
    private fun login() {
        val userName = binding.usernameEt.text.toString()
        val password = binding.passwordEt.text.toString()


        //to check the fields
        if (inputCheck(userName, password)) {
            val user = User(0, userName, password)
            mUserViewModel.addUser(user)

            Toast.makeText(
                requireContext(),
                getString(R.string.welcomeMessage),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().popBackStack(R.id.nav_host, true)
            findNavController().navigate(R.id.newsFragment)

        } else {
            Toast.makeText(requireContext(), getString(R.string.inputCheck), Toast.LENGTH_SHORT)
                .show()
        }

    }

    private fun inputCheck(userName: String, password: String): Boolean {
        return !(TextUtils.isEmpty(userName) || TextUtils.isEmpty(password))
    }
}