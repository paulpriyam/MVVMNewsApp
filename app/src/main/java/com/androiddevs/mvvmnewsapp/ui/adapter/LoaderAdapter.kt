package com.androiddevs.mvvmnewsapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import kotlinx.android.synthetic.main.item_loader.view.*

class LoaderAdapter : LoadStateAdapter<LoaderAdapter.LoaderViewHolder>() {
    override fun onBindViewHolder(holder: LoaderViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoaderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loader, parent, false)
        return LoaderViewHolder(view)
    }

    inner class LoaderViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(loadState: LoadState) {
            view.progressBar.isVisible = loadState is LoadState.Loading
        }
    }
}