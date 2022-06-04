package com.eachilin.zotes.modal

import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class UserModal(
    @get:Exclude
    var id: String = "",
    var username: String =""
) :Serializable