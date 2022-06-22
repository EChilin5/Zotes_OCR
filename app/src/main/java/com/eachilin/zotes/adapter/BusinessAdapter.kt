package com.eachilin.zotes.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eachilin.zotes.activity.PokemonDescription
import com.eachilin.zotes.api.BusinessSearchResultItem
import com.eachilin.zotes.databinding.ItemRestaurantBinding

class BusinessAdapter(private val businessInfo: MutableList<BusinessSearchResultItem>) : RecyclerView.Adapter<BusinessAdapter.PokemonViewHolder>() {

    private var _binding:  ItemRestaurantBinding?=null
    private val binding get() = _binding!!

    class PokemonViewHolder(itemView: ItemRestaurantBinding) : RecyclerView.ViewHolder(itemView.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: BusinessSearchResultItem, binding: ItemRestaurantBinding) {
            val tvName: TextView =   binding.tvProductName
            val ivPokemon: ImageView = binding.ivProduct
            val clPokemonDetail = binding.clProduct
            val tvPrice = binding.tvProductCost
            val imageLink = item.image

            tvPrice.text = "$ ${item.price}"
            var title = item.title

            title = getTitle(title)


            tvName.text = title

            Glide.with(itemView.context)
                .load(imageLink)
//                .override(, 100)
                .centerInside()
                .into(ivPokemon)

            clPokemonDetail.setOnClickListener {
                val intent = Intent(itemView.context, PokemonDescription::class.java)
                intent.putExtra("businessProduct", item)
                itemView.context.startActivity(intent)
            }

        }

        private fun getTitle(title: String):String {
            val separate = " "
            val strList = title.split(separate)
            var word = ""
            var count = 0
            for(item in strList){
                word += "$item "
                count++
                if(count == 5){
                    word = word.trim() + "..."
                    break
                }


            }
            return word

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
