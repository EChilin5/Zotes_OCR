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
class CheckoutAdapter(private val poke:MutableList<CartItemModal>): RecyclerView.Adapter<CheckoutAdapter.CartViewHolder>() {

    private var _binding: ItemCheckoutBinding?=null
    private val binding get() = _binding!!





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
    _binding = ItemCheckoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val poke = poke[position]
        holder.bind(poke)
    }

    override fun getItemCount(): Int {
       return poke.size
    }

    class CartViewHolder(itemView: ItemCheckoutBinding) : RecyclerView.ViewHolder(itemView.root) {
        @SuppressLint("SetTextI18n")
        fun bind(poke: CartItemModal) {
            var pokeImg :ImageView = itemView.findViewById(R.id.ivCheckPokemon)
            var name:TextView = itemView.findViewById(R.id.tvCheckPokeNameCart)
            var cost:TextView = itemView.findViewById(R.id.tvCheckCost)

            name.text = poke.name
            var pokeCost  = poke.pokeID?.toInt()?.times(15)
            Log.e(TAG, "$pokeCost + first")
            pokeCost = pokeCost?.times(poke.count)
            Log.e(TAG, "$pokeCost + seconf + ${poke.count}")

            cost.text = "$ $pokeCost"
            var pokeID = poke.pokeID
            var pokemonLink =
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/$pokeID.png"

            Glide.with(itemView.context)
                .load(pokemonLink)
//                .override(, 100)
                .into(pokeImg)
        }

    }


}