package com.eachilin.zotes.pokemon

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonDetailService {
    @GET("pokemon/{number}")
    fun PokemonDetailedInfo(
        @Path("number") name:String
    ) : Call<PokemonDetailedSearchResult>
}

interface PokeDexInformation {
    @GET("pokemon-species/{number}")
    fun PokedexEntry(
        @Path("number") name:String
    ):Call<PokemonPokedexEntry>
}




