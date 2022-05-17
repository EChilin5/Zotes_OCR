package com.eachilin.zotes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eachilin.zotes.databinding.ItemReviewsBinding
import com.eachilin.zotes.modal.ReviewModal

class ReviewAdapter (private val reviewPost: MutableList<ReviewModal>) : RecyclerView.Adapter<ReviewAdapter.ReviewPostViewHolder>(){

    private var _binding : ItemReviewsBinding? = null
    private val binding get() = _binding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewPostViewHolder {
        _binding = ItemReviewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewPostViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: ReviewPostViewHolder, position: Int) {
        val post = reviewPost[position]
        holder.bind(post, binding)
    }

    override fun getItemCount(): Int {
        return reviewPost.size
    }

    inner class ReviewPostViewHolder(itemView: ItemReviewsBinding) : RecyclerView.ViewHolder(itemView.root) {
        fun bind(post: ReviewModal, binding: ItemReviewsBinding?) {
            var tvName = binding?.tvReviewName
            var tvRating = binding?.tvRating
            var tvDescription = binding?.tvReviewDescription

            tvName?.text = post.name
            tvRating?.text = post.rating
            tvDescription?.text = post.description
        }

    }
}