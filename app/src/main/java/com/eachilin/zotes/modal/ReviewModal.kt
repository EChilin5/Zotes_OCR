package com.eachilin.zotes.modal

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.io.Serializable

data class ReviewModal (
    @DocumentId
    val id: String?=null,
    var name:String?="",
    var rating:String="",
    var description:String="",
    var pokemonID:String="",
    @get:PropertyName("creation_time_ms")  @set:PropertyName("creation_time_ms") var creationTime : Long = 0,
    var user : UserModal? = null
) : Serializable