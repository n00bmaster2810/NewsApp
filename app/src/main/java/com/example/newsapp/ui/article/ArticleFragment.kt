package com.example.newsapp.ui.article

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.viewmodels.NewsViewModel
import com.example.newsapp.viewmodels.UserViewModel

class ArticleFragment : Fragment(R.layout.fragment_article) {

    private lateinit var binding: FragmentArticleBinding
    private val args: ArticleFragmentArgs by navArgs()
    private lateinit var mNewsViewModel: NewsViewModel
    private lateinit var mUserViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        mNewsViewModel = (activity as MainActivity).newsViewModel

        mUserViewModel = (activity as MainActivity).userViewModel

        val article = args.article

        binding.apply {
            titleTv.text = article.title
            sourceTv.text = article.source?.name ?: "anonymous"
            timeTv.text = article.publishedAt
            descriptionTv.text = article.description
            contentTv.text = article.content
            urlTv.text = "${getString(R.string.visit)} ${ article.url }"
        }
        Glide.with(this).load(article.urlToImage).into(binding.imageView)
    }

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