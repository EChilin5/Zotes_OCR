package com.eachilin.zotes.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BusinessSearchResult(
    @SerializedName("")var products : List<BusinessSearchResultItem>
) :Serializable

data class BusinessSearchResultItem(
    var category: String,
    var description: String,
    var id: Int,
    var image: String,
    var price: Double,
    var rating: Rating,
    var title: String
):Serializable

data class Rating(
    var count: Int,
    var rate: Double
):Serializable