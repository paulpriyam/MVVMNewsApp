package com.androiddevs.mvvmnewsapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.model.Article
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_article_preview.view.*

class PagingNewsAdapter :
    PagingDataAdapter<Article, PagingNewsAdapter.NewsViewHolder>(diffUtilCallback) {

    companion object {
        val diffUtilCallback = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.url == newItem.url
            }
        }
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article_preview, parent, false)
        return NewsViewHolder(view)
    }


    inner class NewsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data: Article) {
            Glide.with(view.ivArticleImage)
                .load(data.urlToImage)
                .placeholder(R.drawable.ic_image_placeholder)
                .into(view.ivArticleImage)
            view.tvDescription.text = data.description
            view.tvSource.text = data.source.name
            view.tvTitle.text = data.title
            view.tvPublishedAt.text = data.publishedAt
            view.setOnClickListener {
                onItemClickListener?.let {
                    it(data)
                }
            }
        }
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: ((Article) -> Unit)) {
        onItemClickListener = listener
    }

}