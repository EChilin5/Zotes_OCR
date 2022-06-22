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




private const val TAG = "Abilities"
class Abilities(var business: BusinessSearchResultItem) : Fragment() {

//    private val pokemon = mutableListOf<PokemonDetailedSearchResult>()

    private var _binding : FragmentAbilitiesBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAbilitiesBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var product = this.business
        binding.tvCategory.text ="Category: ${product.category}"
        binding.tvPrice.text = "Price: $ ${product.price}"
        binding.tvRating.text = "Product Rating ${product.rating.rate}/5"
        binding.tvStock.text = "Only ${product.rating.count} items left"

    }


}