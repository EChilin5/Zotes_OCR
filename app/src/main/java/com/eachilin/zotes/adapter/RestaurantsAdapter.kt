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
import com.eachilin.zotes.databinding.ItemRestaurantBinding
import com.eachilin.zotes.pokemon.PokemonInitialData

class RestaurantsAdapter(private val restaurant: MutableList<PokemonInitialData>) : RecyclerView.Adapter<RestaurantsAdapter.PokemonViewHolder>() {

    private var _binding:  ItemRestaurantBinding?=null
    private val binding get() = _binding!!

    class PokemonViewHolder(itemView: ItemRestaurantBinding) : RecyclerView.ViewHolder(itemView.root) {
        @SuppressLint("SetTextI18n")
        fun bind(restaurant: PokemonInitialData) {
            var tvName: TextView =   itemView.findViewById(R.id.tvName)
            var ivPokemon: ImageView = itemView.findViewById(R.id.ivPokemon)
            var clPokemonDelail = itemView.findViewById<ConstraintLayout>(R.id.clPokemonDetail)
            var tvPrice = itemView.findViewById<TextView>(R.id.tvHPokeCost)
            var pokeID = restaurant.url
            pokeID = pokeID.replace("https://pokeapi.co/api/v2/pokemon/", "")
            pokeID = pokeID.replace("/", "")
            var pokemonLink =
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/" + pokeID + ".png"

            tvPrice.text = "$ " + (pokeID?.toInt()?.times(15)).toString()
            tvName.text = restaurant.name

            Glide.with(itemView.context)
                .load(pokemonLink)
//                .override(, 100)
                .into(ivPokemon)

            clPokemonDelail.setOnClickListener {
                val intent = Intent(itemView.context, PokemonDescription::class.java)
                intent.putExtra("Image", pokemonLink)
                intent.putExtra("Name", restaurant.name)
                intent.putExtra("Id", pokeID)
                itemView.context.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {

        _binding = ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val restaurant = restaurant[position]
        holder.bind(restaurant)
    }

    override fun getItemCount(): Int {
        return restaurant.size
    }


}
