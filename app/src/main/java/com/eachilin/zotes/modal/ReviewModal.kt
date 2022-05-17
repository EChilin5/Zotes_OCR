package com.eachilin.zotes.modal

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class ReviewModal (
    @DocumentId
    val id: String?=null,
    var name:String?="",
    var rating:String="",
    var description:String=""
) : Serializable