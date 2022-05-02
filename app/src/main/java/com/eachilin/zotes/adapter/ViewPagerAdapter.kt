package com.eachilin.zotes.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.eachilin.zotes.pokemondetailfragments.Abilities
import com.eachilin.zotes.pokemondetailfragments.PokemonAdditionalDetails
import com.eachilin.zotes.pokemondetailfragments.ShoppingDetails

class ViewPagerAdapter(
    var fragmnetManager: FragmentManager,
    lifecycle: Lifecycle,
    var name: String,
    var id: String
): FragmentStateAdapter(fragmnetManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        val args = Bundle()
        args.putString("name", this.name)
        args.putString("id", this.id)

      return when(position){
           0->{
               Abilities(this.name, this.id)
           }
           1->{
               PokemonAdditionalDetails(this.name, this.id)
           }
           2->{
               ShoppingDetails()
           }
           else->{
               Fragment()
           }
       }
    }
}