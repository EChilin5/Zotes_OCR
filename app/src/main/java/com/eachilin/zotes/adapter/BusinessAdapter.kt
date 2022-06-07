package com.eachilin.zotes.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eachilin.zotes.R
import com.eachilin.zotes.activity.PokemonDescription
import com.eachilin.zotes.api.BusinessSearchResultItem
import com.eachilin.zotes.databinding.ItemRestaurantBinding

class BusinessAdapter(private val businessInfo: MutableList<BusinessSearchResultItem>) : RecyclerView.Adapter<BusinessAdapter.PokemonViewHolder>() {

    private var _binding:  ItemRestaurantBinding?=null
    private val binding get() = _binding!!

    class PokemonViewHolder(itemView: ItemRestaurantBinding) : RecyclerView.ViewHolder(itemView.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: BusinessSearchResultItem, binding: ItemRestaurantBinding) {
            var tvName: TextView =   binding.tvProductName
            var ivPokemon: ImageView = binding.ivProduct
            var clPokemonDelail = binding.clProduct
            var tvPrice = binding.tvProductCost
            var imageLink = item.image

            tvPrice.text = "$ ${item.price}"
            tvName.text = item.title

            Glide.with(itemView.context)
                .load(imageLink)
//                .override(, 100)
                .centerInside()
                .into(ivPokemon)

            clPokemonDelail.setOnClickListener {
                val intent = Intent(itemView.context, PokemonDescription::class.java)
                intent.putExtra("businessProduct", item)
                itemView.context.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {

        _binding = ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val businessItem = businessInfo[position]
        holder.bind(businessItem, binding)
    }

    override fun getItemCount(): Int {
        return businessInfo.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}
