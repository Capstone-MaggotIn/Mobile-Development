package com.capstone.maggotin.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.maggotin.R
import com.capstone.maggotin.data.ResultArticle
import com.capstone.maggotin.databinding.FragmentHomeBinding
import com.capstone.maggotin.ui.ArticleAdapter
import com.capstone.maggotin.ui.login.LoginActivity
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        observeArticles()

        viewModel.getSession().observe(viewLifecycleOwner, Observer { user ->
            if (!user.isLogin) {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            } else {
                observeArticles()
            }
        })

        return root
    }

    private fun setupRecyclerView() {
        articleAdapter = ArticleAdapter { article ->
            Toast.makeText(requireContext(), "Bookmarked ${article.title}", Toast.LENGTH_SHORT).show()
            val newBookmarkState = article.isBookmarked
            viewModel.setBookmarkedArticle(article, newBookmarkState)
            Log.d("Bookmark", "Article bookmarked: ${article.title} - ${article.isBookmarked}")
        }
        binding.rvArticles.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = articleAdapter
        }
    }

    private fun observeArticles() {
        viewModel.getArticles().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is ResultArticle.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ResultArticle.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val articleData = result.data
                    articleAdapter.submitList(articleData)
                }
                is ResultArticle.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_logout -> {
                viewModel.logout()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}