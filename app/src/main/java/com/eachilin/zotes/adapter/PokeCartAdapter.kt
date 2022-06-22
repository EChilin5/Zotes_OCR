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
import com.eachilin.zotes.databinding.PokeShoppingBinding
import com.eachilin.zotes.menufragments.CartFragment
import com.eachilin.zotes.modal.CartItemModal
import com.eachilin.zotes.modal.CartModal
import java.math.BigDecimal
import java.math.RoundingMode

private const val TAG = "PokeCartAdapter"
class PokeCartAdapter(
    private var productInfo: ArrayList<CartItemModal>,
    var onItemClicked: (CartItemModal) -> Unit,
    var onIncrementPrice : (Int, Double, Int, String) -> Unit,
    var onDecrementPrice: (Int, Double, Int, String) -> Unit
): RecyclerView.Adapter<PokeCartAdapter.CartViewHolder>() {

    private var _binding: PokeShoppingBinding?=null
    private val binding get() = _binding!!


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
        fun bind(
            position: Int,
            product: CartItemModal,
            onIncrementPrice: (Int, Double, Int, String) -> Unit,
            onDecrementPrice: (Int, Double, Int, String) -> Unit,
            binding: PokeShoppingBinding
        ) {

            val tvPokemonName : TextView = binding.tvPokeNameCart
            val tvCost : TextView = binding.tvCost
            val pokeImg: ImageView = binding.ivOrderPokemon
            val tvRemove:ImageView = binding.ivRemove
            val tvAmount:TextView = binding.tvAmount
            val tvAdd:ImageView = binding.ivAdd

            tvAmount.text = product.count.toString()

            // imageView
            val imageLink = product.image
            //
           // first load
            tvPokemonName.text = product.name
            // price
            val price = product.price.times(product.count)
            tvCost.text = "$ $price"

            // remove
            tvRemove.setOnClickListener{
                if(product.count != 1){
                    Log.e(TAG, "removed")
                    val array = updateItemCount(product.price, product.count, false)
                    val newCount = array[0].toInt()
                    val newPrice = array[1]
                    onDecrementPrice(position, newPrice, newCount, product.id.toString() )
                    Log.e(TAG, "added amount ${array[0]}  +  price${array[1]} ")
                    tvAmount.text = array[0].toString()
                    tvCost.text = array[1].toString()

                }


            }
            // add
            tvAdd.setOnClickListener {
                Log.e(TAG, "added")
                val array = updateItemCount(product.price, product.count, true,)
                val newCount = array[0].toInt()

                val newPrice = array[1]
                onIncrementPrice(position, newPrice, newCount, product.id.toString() )
                Log.e(TAG, "added amount ${array[0]}  +  price${array[1]} ")
                tvAmount.text = newCount.toString()
                tvCost.text = newPrice.toString()
            }

            Glide.with(itemView.context)
                .load(imageLink)
                .into(pokeImg)
        }

        @SuppressLint("SetTextI18n")
        private fun updateItemCount(
            price: Double,
            count: Int,
            isIncreased: Boolean, ): DoubleArray {
            val itemValue = doubleArrayOf(0.0, 0.0)
            val amount: Int = if(isIncreased){
                count+1
            }else{
                count -1
            }

            Log.e(TAG, "updateItemCount function ")

            // price
            val price = price.times(amount)

            itemValue[0] = amount.toDouble()
            val finalcount = BigDecimal(price).setScale(2, RoundingMode.HALF_EVEN)

            itemValue[1] = finalcount.toDouble()

            return itemValue

        }





    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
//        sqlCartHelper = (parent.context)?.let { CartHelper() }

          _binding = PokeShoppingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        val poke = productInfo[position]
        holder.bind(position, poke, onIncrementPrice, onDecrementPrice, binding)


        val btnRemove:ImageView = holder.itemView.findViewById(R.id.ivCtDelete)
        btnRemove.setOnClickListener {
           onItemClicked(poke)
            notifyItemRemoved(position)
        }

    }

    override fun getItemCount(): Int {
        return productInfo.size
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}