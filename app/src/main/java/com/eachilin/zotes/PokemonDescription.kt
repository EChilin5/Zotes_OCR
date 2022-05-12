package com.eachilin.zotes

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.eachilin.zotes.DBhelper.CartHelper
import com.eachilin.zotes.adapter.ViewPagerAdapter
import com.eachilin.zotes.databinding.ActivityPokemonDescriptionBinding
import com.eachilin.zotes.modal.CartModal
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


private const val TAG:String="PokemonDescription"
class PokemonDescription : AppCompatActivity() {

    private lateinit var binding: ActivityPokemonDescriptionBinding


    private lateinit var sqlCartHelper: CartHelper

    override fun onStart() {
        super.onStart()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var name:String = ""
        var image:String = ""
        var id:String = ""
        sqlCartHelper = CartHelper(this)

        val extras = intent.extras
        if (extras != null) {
             name = extras.getString("Name").toString()
             image = extras.getString("Image").toString()
             id = extras.getString("Id").toString()
            //The key argument here must match that used in the other activity
            binding.tvPokemonName.text = name
            binding.tvPokemonPrice.text = "$ " + (id!!.toInt() * 15).toString();
            Glide.with(this)
                .load(image)
//                .override(, 100)
                .into(binding.ivPokemonDesc)
        }


         var pokemonExistInCart =   sqlCartHelper.checkPokemonExist(id)



        val btnAddToCart = binding.btnPokeAddToCart

        val btnBuyNos = binding.btnBuyNow

        if(pokemonExistInCart){
            btnAddToCart.isEnabled = false
        }

        Toast.makeText(this, "$pokemonExistInCart", Toast.LENGTH_SHORT).show()

        val cost = "$ " + (id!!.toInt() * 15).toString();
        btnAddToCart.setOnClickListener { addPokemon(id, name, image) }
        btnBuyNos.setOnClickListener { openCheckout(image, name, cost) }


        val tabLayout = binding.tabLayout
        val viewPager2 = findViewById<ViewPager2>(R.id.view_pokemonInfo)

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, name, id)

        viewPager2.adapter = adapter

        val args = Bundle()
        args.putString("name", name)
        args.putString("id", id)



        TabLayoutMediator(tabLayout, viewPager2){ tab, position->
            when(position){
                0->{
                    tab.text="Abilities"
                }
                1->{
                    tab.text="Details"
                }
                2->{
                    tab.text="Shopping"
                }

        }
        }.attach()

    }

    private fun openCheckout(  pokeImage :String, name:String, cost:String ) {
        val args = Bundle()
        args.putString("image", pokeImage)
        args.putString("name", name)
        args.putString("cost", cost)
        val newFragment = overlay_buy_now()
        newFragment.arguments= args
        newFragment.show(supportFragmentManager, TAG)
    }

    private fun getPokemon() {
        try{
            val pokeList = sqlCartHelper.getAllPokemon()
            Log.i(TAG, "${pokeList.size}")

        }catch (e:Exception){
            Log.e(TAG, e.message.toString())

        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun addPokemon(id: String, name: String, image: String) {
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.BASIC_ISO_DATE
        val formatted = current.format(formatter)

        val pokeListSize = sqlCartHelper.getAllPokemon()
        val primaryID = pokeListSize.size +1

        val pokeList = CartModal(id=primaryID, name = name, pokeID = id, count = 1, orderPlace= "false", purchaseDate = formatted )
        val status = sqlCartHelper.insertStudent(pokeList)
        if(status > -1){
            Toast.makeText(this, "Pokemon Added...", Toast.LENGTH_SHORT).show()
        }else{
                Toast.makeText(this, "Pokemon Not Added...", Toast.LENGTH_SHORT).show()

        }


    }



}