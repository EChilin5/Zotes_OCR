package com.eachilin.zotes

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eachilin.zotes.databinding.ActivityMainBinding
import com.eachilin.zotes.databinding.ActivityPokemonDescriptionBinding
import com.eachilin.zotes.menufragments.CartFragment
import com.eachilin.zotes.menufragments.HomeFragment
import com.eachilin.zotes.menufragments.SettingFragment
import com.eachilin.zotes.pokemon.PokemSearchResult
import com.eachilin.zotes.pokemon.PokemonInitialData
import com.eachilin.zotes.pokemon.PokemonService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val homeFragment = HomeFragment()
    private val cartFragment = CartFragment()
    private val settingFragment = SettingFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.ic_home ->openFragment(homeFragment)
                R.id.ic_cart ->openFragment(cartFragment)
                R.id.ic_setting ->openFragment(settingFragment)

            }
            true
        }
        openFragment(homeFragment)


    }

    private fun openFragment(fragment: Fragment){
        if(fragment != null){
            val transaction =supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }


}