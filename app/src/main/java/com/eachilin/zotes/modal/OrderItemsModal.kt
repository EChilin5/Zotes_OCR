package com.eachilin.zotes.modal

data class OrderItemsModal(
    var name: String,
    var imageURL: String,
    var price: Long?,
    var amount:Int
)