package com.capstone.maggotin.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.maggotin.R
import com.capstone.maggotin.data.local.entity.ArticlesEntity
import com.capstone.maggotin.databinding.ItemArticlesBinding
import com.capstone.maggotin.ui.article.DetailArticleActivity
import com.capstone.maggotin.utils.DateFormatter

class ArticleAdapter(private val onBookmarkClick: (ArticlesEntity) -> Unit) :
    ListAdapter<ArticlesEntity, ArticleAdapter.ArticleViewHolder>(ArticleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticlesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)

        val ivBookmark = holder.binding.ivBookmark

        if (article.isBookmarked) {
            ivBookmark.setImageDrawable(ContextCompat.getDrawable(ivBookmark.context, R.drawable.ic_bookmarked))
        } else {
            ivBookmark.setImageDrawable(ContextCompat.getDrawable(ivBookmark.context, R.drawable.ic_bookmarks))
        }

        ivBookmark.setOnClickListener {
            article.isBookmarked = !article.isBookmarked
            onBookmarkClick(article)
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailArticleActivity::class.java)
            intent.putExtra("URL", article.linkArticle)
            context.startActivity(intent)
        }
    }

    inner class ArticleViewHolder(val binding: ItemArticlesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: ArticlesEntity) {
            Glide.with(binding.imgPoster.context)
                .load(article.image)
                .placeholder(R.drawable.ic_load)
                .error(R.drawable.ic_image_broken)
                .into(binding.imgPoster)

            binding.tvItemTitle.text = article.title
            binding.tvDesc.text = article.description
            binding.tvItemPublishedDate.text = DateFormatter.formatDate(article.date)

            binding.root.setOnClickListener {
                onBookmarkClick(article)
            }
        }
    }

    class ArticleDiffCallback : DiffUtil.ItemCallback<ArticlesEntity>() {
        override fun areItemsTheSame(oldItem: ArticlesEntity, newItem: ArticlesEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ArticlesEntity, newItem: ArticlesEntity): Boolean {
            return oldItem == newItem
        }
    }
}