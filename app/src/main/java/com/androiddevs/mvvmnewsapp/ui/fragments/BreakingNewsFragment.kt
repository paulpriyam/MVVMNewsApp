package com.androiddevs.mvvmnewsapp.ui.fragments

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.NewsActivity
import com.androiddevs.mvvmnewsapp.ui.adapter.LoaderAdapter
import com.androiddevs.mvvmnewsapp.ui.adapter.NewsAdapter
import com.androiddevs.mvvmnewsapp.ui.adapter.PagingNewsAdapter
import com.androiddevs.mvvmnewsapp.ui.viewmodel.NewsViewModel
import com.androiddevs.mvvmnewsapp.utils.converters.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_breaking_news.view.*

@AndroidEntryPoint
class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    private val viewModel: NewsViewModel by activityViewModels()
    private lateinit var pagingNewsAdapter: PagingNewsAdapter

    val TAG = "BreakingNewsFragment"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_breaking_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        viewModel.breakingNews.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    hideProgressBar()
                    clErrorView.isVisible = true
                    result.message?.let {
                        Log.e(TAG, "Error occurred ${it}")
                    }
                }
                is Resource.Success -> {
                    hideProgressBar()
                    clErrorView.isVisible = false
                    viewModel.pagerBreakingNews?.observe(viewLifecycleOwner) {
                        pagingNewsAdapter.submitData(lifecycle, it)
                    }

                }
                is Resource.Loading -> {
                    showProgressBar()
                    clErrorView.isVisible = false
                }
            }
        }
        pagingNewsAdapter.setOnItemClickListener {
            val bundle = Bundle()
            bundle.apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment, bundle
            )
        }
    }

    private fun initRecyclerView() {
        pagingNewsAdapter = PagingNewsAdapter()
        rvBreakingNews.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter =
                pagingNewsAdapter.withLoadStateHeaderAndFooter(LoaderAdapter(), LoaderAdapter())
        }
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        viewModel.getBreakingNews("us")
    }
}