package com.eachilin.zotes.modal

import java.io.Serializable

data class OrderItemsModal(
    var name: String = "",
    var imageURL: String="",
    var price: Long?=0,
    var amount:Int=0
): Serializable