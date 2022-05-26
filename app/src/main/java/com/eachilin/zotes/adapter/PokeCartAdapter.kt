package com.eachilin.zotes.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eachilin.zotes.R
import com.eachilin.zotes.databinding.PokeShoppingBinding
import com.eachilin.zotes.menufragments.CartFragment
import com.eachilin.zotes.modal.CartModal

private const val TAG = "PokeCartAdapter"
class PokeCartAdapter(
    private var poke: ArrayList<CartModal>,
    var onItemClicked: (CartModal) -> Unit,
    var onIncrementPrice : (String, Int, Int) -> Unit,
    var onDecrementPrice: (String, Int, Int) -> Unit
): RecyclerView.Adapter<PokeCartAdapter.CartViewHolder>() {

    private var _binding: PokeShoppingBinding?=null
    private val binding get() = _binding!!

    private lateinit  var sqlCartHelper: CartFragment

    private var onClickItem: ((CartModal)-> Unit)? = null
    private var onClickDeleteItem: ((CartModal) -> Unit)? = null

    fun setOnClickItem( callback: (CartModal) -> Unit){
        this.onClickItem = callback
    }

    fun setOnClickDeleteItem(callback:(CartModal) -> Unit){
        this.onClickDeleteItem = callback
    }

    class CartViewHolder(itemView: PokeShoppingBinding) : RecyclerView.ViewHolder(itemView.root) {
        @SuppressLint("SetTextI18n")
        fun bind(poke: CartModal, onIncrementPrice: (String,Int, Int) -> Unit, onDecrementPrice: (String,Int, Int) -> Unit) {

            var tvPokemonName : TextView = itemView.findViewById(R.id.tvPokeNameCart)
            var tvCost : TextView = itemView.findViewById(R.id.tvCost)
            var pokeImg: ImageView = itemView.findViewById(R.id.ivOrderPokemon)
            var tvRemove:ImageView = itemView.findViewById(R.id.ivRemove)
            var tvAmount:TextView = itemView.findViewById(R.id.tvAmount)
            var tvAdd:ImageView = itemView.findViewById(R.id.ivAdd)

            tvAmount.text = poke.count.toString()
            var amount: Int = (tvAmount.text as String).toInt()

            var pokeID = poke.pokeID

            var pokemonLink =
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/$pokeID.png"

            tvPokemonName.text = poke.name
            var price = pokeID?.toInt()?.times(15)
            price = price?.let { amount.toInt().times(it) }
            tvCost.text = "$ $price"

            tvRemove.setOnClickListener { if(amount > 0){
                amount--;
                tvAmount.text = amount.toString()

                var price = pokeID?.toInt()?.times(15)
                onDecrementPrice(poke.id.toString(), price!!.toInt(), amount )
                price = price?.let { amount.toInt().times(it) }
                tvCost.text = "$ $price"

            }
            }
            tvAdd.setOnClickListener { amount++
                tvAmount.text = amount.toString()
                var price = pokeID?.toInt()?.times(15)
                onIncrementPrice(poke.id.toString(), price!!.toInt(), amount)
                price = price?.let { amount.toInt().times(it) }
                tvCost.text = "$ $price"
            }

            Glide.with(itemView.context)
                .load(pokemonLink)
                .into(pokeImg)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
//        sqlCartHelper = (parent.context)?.let { CartHelper() }

          _binding = PokeShoppingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        val poke = poke[position]
        holder.bind(poke, onIncrementPrice, onDecrementPrice)


        var btnRemove:ImageView = holder.itemView.findViewById(R.id.ivCtDelete)
        btnRemove.setOnClickListener {
           onItemClicked(poke)
            notifyItemRemoved(position)
        }

    }

    override fun getItemCount(): Int {
        return poke.size
    }
}