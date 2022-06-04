package com.eachilin.zotes.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.eachilin.zotes.api.BusinessSearchResultItem
import com.eachilin.zotes.pokemondetailfragments.Abilities
import com.eachilin.zotes.pokemondetailfragments.PokemonAdditionalDetails
import com.eachilin.zotes.pokemondetailfragments.ShoppingDetails

class ViewPagerAdapter(
    var fragmnetManager: FragmentManager,
    lifecycle: Lifecycle,
    var product: BusinessSearchResultItem,

    ): FragmentStateAdapter(fragmnetManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var businessSearchResultItem = this.product
        var id = this.product.id.toString()


      return when(position){
           0->{
               Abilities(businessSearchResultItem)
           }
           1->{
               PokemonAdditionalDetails(businessSearchResultItem)
           }
           2->{
               ShoppingDetails(id)
           }
           else->{
               Fragment()
           }
       }
    }
}