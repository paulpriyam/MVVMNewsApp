package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.NewsActivity
import com.androiddevs.mvvmnewsapp.ui.adapter.NewsAdapter
import com.androiddevs.mvvmnewsapp.ui.adapter.PagingNewsAdapter
import com.androiddevs.mvvmnewsapp.ui.viewmodel.NewsViewModel
import com.androiddevs.mvvmnewsapp.utils.converters.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    private val viewModel: NewsViewModel by activityViewModels()

    //    private lateinit var newsAdapter: NewsAdapter
    private lateinit var pagingNewsAdapter: PagingNewsAdapter

    private val TAG = "SearchNewsFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_search_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        var job: Job? = null
        etSearch.addTextChangedListener { searchedText ->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                searchedText?.let {
                    if (it.toString().isNotEmpty()) {
                        viewModel.getSearchedNews(it.toString())
                    }
                }
            }
        }
        viewModel.searchNews.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    hideProgressBar()
                    ivEmptyView.isVisible = true
                    result.message?.let {
                        Log.e(TAG, "Error occurred ${it}")
                    }
                }
                is Resource.Success -> {
                    hideProgressBar()
                    ivEmptyView.isVisible = false
                    viewModel.pagerSearchNews?.observe(viewLifecycleOwner) {
                        pagingNewsAdapter.submitData(lifecycle, it)
                    }
//                    newsAdapter.differ.submitList(result.data?.articles)
                }
                is Resource.Loading -> {
                    showProgressBar()
                    ivEmptyView.isVisible = false
                }
            }
        }
        pagingNewsAdapter.setOnItemClickListener {
            val bundle = Bundle()
            bundle.apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment, bundle
            )
        }
    }

    private fun initRecyclerView() {
        pagingNewsAdapter = PagingNewsAdapter()
        rvSearchNews.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = pagingNewsAdapter
        }
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
    }
}