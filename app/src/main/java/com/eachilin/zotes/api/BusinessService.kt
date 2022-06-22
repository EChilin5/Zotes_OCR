package com.eachilin.zotes.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BusinessService {
    // set default to 20
    @GET("products?")
    fun businessInfo(
        @Query("limit") limit:String
    ) : Call<List<BusinessSearchResultItem>>
}