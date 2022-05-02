package com.eachilin.zotes.modal
import java.io.Serializable


data class CartModal (
    var id:Int=0,
    var name:String?="",
    var pokeID:String?="",
    var orderPlace:String?="",
    var purchaseDate:String?="",
        ) : Serializable





