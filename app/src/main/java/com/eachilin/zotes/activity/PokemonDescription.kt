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



private const val TAG:String="PokemonDescription"
class PokemonDescription : AppCompatActivity() {

    private lateinit var binding: ActivityPokemonDescriptionBinding


    private lateinit var sqlCartHelper: CartHelper
    private lateinit var firestore :FirebaseFirestore
    private lateinit var product : BusinessSearchResultItem


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firestore = FirebaseFirestore.getInstance()
        sqlCartHelper = CartHelper(this)

        product = intent.getSerializableExtra("businessProduct") as BusinessSearchResultItem
        val id  = product.id.toString()
        val name: String = product.title
        val image : String = product.image
            //The key argument here must match that used in the other activity
            binding.tvPokemonName.text = name

            Glide.with(this)
                .load(image)
                .into(binding.ivPokemonDesc)

        val email = getEmail()
        threadCouritineCheckItem(name, email)

        val btnAddToCart = binding.btnPokeAddToCart

        val btnBuyNos = binding.btnBuyNow


        binding.btnBack.setOnClickListener {

            val intent =Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        btnAddToCart.setOnClickListener { addOrder(id, name, image) }
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
    private fun addOrder(id: String, name: String, image: String) {
        val user = UserModal("", getEmail())

            val cartItem = CartItemModal("", name = name, itemId = product.id.toString(), count = 1, price= product.price, product.image, user )
        firestore.collection("zotesOrderCart").add(cartItem).addOnCompleteListener {
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