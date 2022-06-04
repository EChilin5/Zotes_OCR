package com.eachilin.zotes.modal

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class CartItemModal(
    @DocumentId
    val id: String?=null,
    var name:String?="",
    var itemId:String?="",
    var count:Int =1,
    var price:Double=0.0,
    var image:String="",
    var user:UserModal?=null
):Serializable