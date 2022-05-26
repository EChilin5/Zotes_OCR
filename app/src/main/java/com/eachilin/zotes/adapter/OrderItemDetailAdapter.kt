package com.eachilin.zotes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eachilin.zotes.databinding.ItemOrderAdditionalInforBinding
import com.eachilin.zotes.modal.OrderItemsModal

class OrderItemDetailAdapter(private var item: List<OrderItemsModal>) : RecyclerView.Adapter<OrderItemDetailAdapter.ItemViewHolder>(){

    private var _binding: ItemOrderAdditionalInforBinding?=null
    private val binding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        _binding = ItemOrderAdditionalInforBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val orderItem = item[position]
        holder.bind(orderItem, binding)

    }

    override fun getItemCount(): Int {
        return item.size
    }



    class ItemViewHolder(itemView : ItemOrderAdditionalInforBinding) : RecyclerView.ViewHolder(itemView.root){
        fun bind(orderItem: OrderItemsModal, binding: ItemOrderAdditionalInforBinding) {
            binding.tvOAAmt.text = orderItem.amount.toString()
            binding.tvOAName.text = orderItem.name
            binding.tvOAPrice.text = orderItem.price.toString()
            Glide.with(itemView.context)
                .load(orderItem.imageURL)
                .into(binding.imageView)

        }

    }
}