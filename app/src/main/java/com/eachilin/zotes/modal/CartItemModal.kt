package com.eachilin.zotes.modal

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class CartItemModal(
    @DocumentId
    val id: String?=null,
    var name:String?="",
    var pokeID:String?="",
    var count:Int =1,
    var user:UserModal?=null
):Serializable