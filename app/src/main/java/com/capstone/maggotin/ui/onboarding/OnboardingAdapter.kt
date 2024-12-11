package com.capstone.maggotin.ui.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.maggotin.R

class OnboardingAdapter(private val list: List<OnboardingItem>) : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_onboarding, parent, false)
        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    class OnboardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val titleText: TextView = itemView.findViewById(R.id.titleText)
        private val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)

        fun bind(item: OnboardingItem) {
            imageView.setImageResource(item.image)
            titleText.text = item.title
            descriptionText.text = item.description
        }
    }
}
