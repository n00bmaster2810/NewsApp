package com.example.newsapp.ui.news

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentNewsBinding
import com.example.newsapp.util.Resource
import com.example.newsapp.viewmodels.NewsViewModel
import com.example.newsapp.viewmodels.UserViewModel


class NewsFragment : Fragment() {

    private lateinit var mAdapter: NewsAdapter
    private lateinit var mNewsViewModel: NewsViewModel
    private lateinit var mUserViewModel: UserViewModel
    private lateinit var binding: FragmentNewsBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onStart() {
        super.onStart()

        //initialisation of newsViewModel
        mNewsViewModel = (activity as MainActivity).newsViewModel

        //of userViewModel
        mUserViewModel = (activity as MainActivity).userViewModel

        //connecting adapter with the recycler view
        mAdapter = NewsAdapter()
        binding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        //setting the click event for the item in recycler view
        mAdapter.setOnItemClickListener {
            Log.d("NewsFragment", "$it")
            //passing the data from one fragment to other
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_newsFragment_to_articleFragment, bundle)
        }

        lifecycleScope.launchWhenStarted {
            mNewsViewModel.breakingNewsFlow.collect {
                when (it) {
                    is Resource.Success -> {
                        hideProgressBar()
                        it.data?.let { newsResponse ->
                            mAdapter.differ.submitList(newsResponse.articles)
                        }
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        it.message?.let { message ->
                            Toast.makeText(
                                requireContext(),
                                "Error occurred: $message",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e("NewsFragment", "an error: $message")
                        }
                        showProgressBar()
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            }
        }

    }

    // for functionality of progress bar
    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }

    //for functionality of menu item
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            mUserViewModel.deleteUser()
            Toast.makeText(
                requireContext(),
                getString(R.string.thankyouMessage),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().popBackStack(R.id.nav_host, true)
            findNavController().navigate(R.id.homeFragment)
        }
        return super.onOptionsItemSelected(item)
    }


}