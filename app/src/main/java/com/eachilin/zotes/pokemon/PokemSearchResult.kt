package com.eachilin.zotes.pokemon

import com.google.gson.annotations.SerializedName

data class PokemSearchResult (
    @SerializedName("results") val result: List<PokemonInitialData>
        )


data class PokemonInitialData (
    @SerializedName("name") val  name: String,
    @SerializedName("url") val url:String
    )


