package com.eachilin.zotes.pokemondetailfragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eachilin.zotes.api.BusinessSearchResultItem
import com.eachilin.zotes.databinding.FragmentAbilitiesBinding
import com.eachilin.zotes.pokemon.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Abilities.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val BASE_URL = "https://pokeapi.co/api/v2/"
private const val TAG = "Abilities"
class Abilities(var business: BusinessSearchResultItem) : Fragment() {

//    private val pokemon = mutableListOf<PokemonDetailedSearchResult>()

    private var _binding : FragmentAbilitiesBinding? = null
    private val binding get() = _binding!!




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAbilitiesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var product = this.business
        binding.tvCategory.text ="Category: ${product.category}"
        binding.tvPrice.text = "Price: $ ${product.price}"
        binding.tvRating.text = "Product Rating ${product.rating.rate}/5"
        binding.tvStock.text = "Only ${product.rating.count} items left"

    }


}