package com.eachilin.zotes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eachilin.zotes.databinding.ItemOrderBinding
import com.eachilin.zotes.modal.OrderModal

class OrderAdapter(private var orderInfo: MutableList<OrderModal>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private var _binding:ItemOrderBinding?=null
    private val binding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        _binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderInfo[position]
        holder.bind(order, binding)
    }

    override fun getItemCount(): Int {
        return orderInfo.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }




    class OrderViewHolder(itemView : ItemOrderBinding): RecyclerView.ViewHolder(itemView.root) {

        fun bind(order: OrderModal, binding: ItemOrderBinding) {
            val adapter = OrderItemDetailAdapter(order.Item, order.Item.size-1)
            binding.tvOrderNumber.text = "Order #: ${order.id}"
            binding.tvItemOrderDate.text = order.date
            binding.tvOrderItemPrice.text = "Total Price: $${order.TotalPrice}"

            binding.rvAdditionalOrderInfo.adapter = adapter
            binding.rvAdditionalOrderInfo.layoutManager=LinearLayoutManager(itemView.context)



        }

    }


}