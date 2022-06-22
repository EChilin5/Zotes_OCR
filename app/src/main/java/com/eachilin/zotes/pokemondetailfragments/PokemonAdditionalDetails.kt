package com.eachilin.zotes.pokemondetailfragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eachilin.zotes.api.BusinessSearchResultItem
import com.eachilin.zotes.databinding.FragmentPokemonAdditionalDetailsBinding
import com.eachilin.zotes.pokemon.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



private const val BASE_URL = "https://pokeapi.co/api/v2/"
private const val TAG = "PokemonAdditionalDetails"
class PokemonAdditionalDetails(var business: BusinessSearchResultItem) : Fragment() {

    private var _binding : FragmentPokemonAdditionalDetailsBinding? = null
    private val  binding get() = _binding!!

    private val poke = mutableListOf<PokedexInfo>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPokemonAdditionalDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.Details.text = business.description
    }



}