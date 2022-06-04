package com.eachilin.zotes.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eachilin.zotes.R
import com.eachilin.zotes.databinding.ActivityCheckoutBinding
import com.eachilin.zotes.databinding.ItemCheckoutBinding
import com.eachilin.zotes.modal.CartItemModal
import com.eachilin.zotes.modal.CartModal
import com.eachilin.zotes.modal.OrderItemsModal
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

private const val TAG = "CheckoutAdapter"
class CheckoutAdapter(private val productInfo:MutableList<CartItemModal>): RecyclerView.Adapter<CheckoutAdapter.CartViewHolder>() {

    private var _binding: ItemCheckoutBinding?=null
    private val binding get() = _binding!!





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
    _binding = ItemCheckoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = productInfo[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
       return productInfo.size
    }

    class CartViewHolder(itemView: ItemCheckoutBinding) : RecyclerView.ViewHolder(itemView.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: CartItemModal) {
            var pokeImg :ImageView = itemView.findViewById(R.id.ivCheckPokemon)
            var name:TextView = itemView.findViewById(R.id.tvCheckPokeNameCart)
            var cost:TextView = itemView.findViewById(R.id.tvCheckCost)

            name.text = item.name
            var pokeCost  = item.count.times(item.price)


            cost.text = "$ $pokeCost"

            var imageLink = item.image

            Glide.with(itemView.context)
                .load(imageLink)
//                .override(, 100)
                .into(pokeImg)
        }

    }


}