package com.eachilin.zotes.modal

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class OrderModal(
    @DocumentId
    val id: String? = null,
    var date: String = "",
    var TotalPrice: Double = 0.0,
    var userOrderName: String = "",
    var Item: List<OrderItemsModal> = emptyList()
):Serializable