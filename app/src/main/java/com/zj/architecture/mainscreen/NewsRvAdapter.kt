package com.zj.architecture.mainscreen

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.zj.architecture.R
import com.zj.architecture.repository.NewsItem
import com.zj.architecture.utils.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_view.view.*

class NewsRvAdapter(private val listener: (View) -> Unit) :
    ListAdapter<NewsItem, NewsRvAdapter.MyViewHolder>(NewsItemItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(inflate(parent.context, R.layout.item_view, parent), listener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount() = currentList.size

    inner class MyViewHolder(override val containerView: View, listener: (View) -> Unit) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        init {
            itemView.setOnClickListener(listener)
        }

        fun bind(newsItem: NewsItem) =
            with(itemView) {
                itemView.tag = newsItem
                tvTitle.text = newsItem.title
                tvDescription.text = newsItem.description
                ivThumbnail.load(newsItem.imageUrl) {
                    crossfade(true)
                    placeholder(R.mipmap.ic_launcher)
                }
            }
    }

    internal class NewsItemItemCallback : DiffUtil.ItemCallback<NewsItem>() {
        override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
            return oldItem.title == newItem.title
        }
    }
}

