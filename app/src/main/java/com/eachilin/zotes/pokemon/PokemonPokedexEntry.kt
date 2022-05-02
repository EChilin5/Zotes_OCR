package com.eachilin.zotes.pokemon

import com.google.gson.annotations.SerializedName

data class PokemonPokedexEntry (
    @SerializedName("flavor_text_entries") val entries : List<PokedexInfo>
        )

data class PokedexInfo (
    @SerializedName("flavor_text") val info: String
        )
