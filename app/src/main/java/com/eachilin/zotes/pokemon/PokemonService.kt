package com.eachilin.zotes.pokemon

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

public interface PokemonService{

    @GET("?")
    fun PokemonInfo(
        @Query("offset") limit:String,
        @Query("limit") offset :String
    ) : Call<PokemSearchResult>
}