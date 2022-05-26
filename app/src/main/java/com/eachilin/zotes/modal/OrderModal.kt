package com.eachilin.zotes.modal

import com.google.firebase.firestore.DocumentId

data class OrderModal(
    @DocumentId
    val id: String?=null,
    var date: String,
    var TotalPrice:Long,
    var userOrderName :String,
    var Item:List<OrderItemsModal>
    )