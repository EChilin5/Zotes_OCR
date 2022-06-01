package com.eachilin.zotes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
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
        holder.bind(post, binding, position, reviewPost.size-1)
    }

    override fun getItemCount(): Int {
        return reviewPost.size
    }

    inner class ReviewPostViewHolder(itemView: ItemReviewsBinding) : RecyclerView.ViewHolder(itemView.root) {
        fun bind(post: ReviewModal, binding: ItemReviewsBinding?, position: Int, ListLength: Int) {
            var tvName = binding?.tvReviewName
            var rbRating = binding?.rbReviewRating
            var tvDescription = binding?.tvReviewDescription

            if(position ==ListLength){
                binding?.vLine?.isVisible = false
            }

            tvName?.text = post.name
            rbRating?.rating = post.rating.toFloat()
            tvDescription?.text = post.description
        }

    }
}