package com.eachilin.zotes.pokemon

import com.google.gson.annotations.SerializedName

data class PokemonDetailedSearchResult (
    @SerializedName("base_experience") val baseExperience: String,
    @SerializedName("height") val height:String,
    @SerializedName("weight") val weight: String
        )
