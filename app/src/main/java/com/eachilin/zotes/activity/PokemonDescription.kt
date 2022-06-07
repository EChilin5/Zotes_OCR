package com.eachilin.zotes.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.eachilin.zotes.DBhelper.CartHelper
import com.eachilin.zotes.R
import com.eachilin.zotes.adapter.ViewPagerAdapter
import com.eachilin.zotes.api.BusinessSearchResultItem
import com.eachilin.zotes.databinding.ActivityPokemonDescriptionBinding
import com.eachilin.zotes.modal.CartItemModal
import com.eachilin.zotes.modal.UserModal
import com.eachilin.zotes.overlay.overlay_buy_now
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


private const val TAG:String="PokemonDescription"
class PokemonDescription : AppCompatActivity() {

    private lateinit var binding: ActivityPokemonDescriptionBinding


    private lateinit var sqlCartHelper: CartHelper
    private lateinit var firestore :FirebaseFirestore
    private lateinit var product : BusinessSearchResultItem

    override fun onStart() {
        super.onStart()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firestore = FirebaseFirestore.getInstance()
        var name:String = ""
        var image:String = ""
        var id:String = ""
        sqlCartHelper = CartHelper(this)

        product = intent.getSerializableExtra("businessProduct") as BusinessSearchResultItem

             name = product.title

            //The key argument here must match that used in the other activity
            binding.tvPokemonName.text = name

            image = product.image
            Glide.with(this)
                .load(image)
                .into(binding.ivPokemonDesc)




        var email = getEmail()
        threadCouritineCheckItem(name, email)

        val btnAddToCart = binding.btnPokeAddToCart

        val btnBuyNos = binding.btnBuyNow


        binding.btnBack.setOnClickListener {

            var intent =Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Toast.makeText(this, "$pokemonExistInCart", Toast.LENGTH_SHORT).show()

        val cost = "$ ${product.price}"
        btnAddToCart.setOnClickListener { addPokemon(id, name, image) }
        btnBuyNos.setOnClickListener { openCheckout(image, name, product.price.toString()) }


        val tabLayout = binding.tabLayout
        val viewPager2 = findViewById<ViewPager2>(R.id.view_pokemonInfo)

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, product)

        viewPager2.adapter = adapter

        val args = Bundle()
        args.putString("name", name)
        args.putString("id", id)



        TabLayoutMediator(tabLayout, viewPager2){ tab, position->
            when(position){
                0->{
                    tab.text="General"
                }
                1->{
                    tab.text="Description"
                }
                2->{
                    tab.text="Reviews"
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

    private fun threadCouritineCheckItem( itemName: String, email: String)= runBlocking {
        async { isItemInCart(itemName, email) }
//        if(isPresent.await()){
//            binding.btnPokeAddToCart.isEnabled = false
//
//        }
    }

    private fun isItemInCart(itemName:String, email: String){
        var colle =  firestore.collection("zotesOrderCart").whereEqualTo("user.username", email).whereEqualTo("name", itemName).get()

        colle.addOnCompleteListener {
            if(!it.result.isEmpty){
                binding.btnPokeAddToCart.isEnabled = false
                return@addOnCompleteListener
            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun addPokemon(id: String, name: String, image: String) {
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.BASIC_ISO_DATE
//        val formatted = current.format(formatter)
//
//        val pokeListSize = sqlCartHelper.getAllPokemon()


        val user = UserModal("", getEmail())

            val cartItem = CartItemModal("", name = name, itemId = product.id.toString(), count = 1, price= product.price, product.image, user )
        firestore.collection("zotesOrderCart").add(cartItem).addOnCompleteListener { it->
            if(it.isSuccessful){
                Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(this, "Cannot be added to cart", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun getEmail():String{
        val auth = FirebaseAuth.getInstance()
        val emailInfo = auth.currentUser?.email.toString()
        return emailInfo
    }



}