package com.eachilin.zotes.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eachilin.zotes.DBhelper.CartHelper
import com.eachilin.zotes.adapter.CheckoutAdapter
import com.eachilin.zotes.databinding.ActivityCheckoutBinding
import com.eachilin.zotes.modal.CartModal
import com.eachilin.zotes.modal.OrderItemsModal
import com.eachilin.zotes.modal.OrderModal
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

private const val TAG = "Checkout"
class Checkout : AppCompatActivity() {

    private var count by Delegates.notNull<Long>()
    private lateinit var binding : ActivityCheckoutBinding

    private lateinit var etName: EditText
    private lateinit var etAddress: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnBuy: Button
    private lateinit var sqlCartHelper: CartHelper

    private var pokemon = ArrayList<CartModal>()
    private var order = mutableListOf<OrderModal>()
    private var orderItems = mutableListOf<OrderItemsModal>()

    private lateinit var rvCheckout:RecyclerView
    private lateinit var firestoreDB:FirebaseFirestore
    private val adapter = CheckoutAdapter(pokemon)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firestoreDB = FirebaseFirestore.getInstance()
        sqlCartHelper = CartHelper(this)


        rvCheckout = binding.rvCheck
        rvCheckout.adapter = adapter
        rvCheckout.layoutManager = LinearLayoutManager(this)
        fetchData()

        etName = binding.editTextTextPersonName
        etAddress = binding.etAddress
        etEmail = binding.etEmail
        btnBuy = binding.btncBuyNow
        countTotalVal()
        btnBuy.setOnClickListener {
            completeOrder()
        }


    }

    private fun fetchData() {
        val myList: ArrayList<CartModal>? = intent.getSerializableExtra("pokeList") as ArrayList<CartModal>?
        Log.e(TAG, myList.toString())

            pokemon.addAll(myList!!)
            adapter.notifyDataSetChanged()


    }

    private fun countTotalVal(){
         count =0
        if(pokemon.isEmpty()){
            count =0
        }else{
            count = 0
            for(item in pokemon){
                var price = item.pokeID?.toInt()?.times(15)
                price = price!!.times(item.count)
                count += price
            }
        }
        binding.tvcCost.text = "$ $count"
    }

    @SuppressLint("SimpleDateFormat")
    private fun completeOrder(){
        val email =getEmail()
        for(orderInformation in pokemon){
            var itemID = orderInformation.pokeID
            var count = orderInformation.count
            var pokemonLink =
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/$itemID.png"
            var cost = itemID?.toInt()?.times(15)
            cost = cost?.times(orderInformation.count)

            var newOrderItem = OrderItemsModal(orderInformation.name.toString(), pokemonLink,
                cost?.toLong(), count)
            orderItems.add(newOrderItem)
        }

        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = sdf.format(Date())
        var newOrder = OrderModal("", currentDate,  count, email, orderItems  )
        firestoreDB.collection("zotesOrder").add(newOrder)
            .addOnCompleteListener { newZotesOrder->
                if(newZotesOrder.isSuccessful){
                    Log.e(TAG,"uploaded successfully")
                    Toast.makeText(this, "uploadeded", Toast.LENGTH_SHORT).show()
                    pokemon.clear()
                    checkout()

                }else{
                    Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()

                }
            }


    }

    private fun checkout() {
        val name = etName.text.trim().toString()
        val address = etAddress.text.trim().toString()
        val email = etEmail.text.trim().toString()

        if(name.isNotEmpty() && address.isNotEmpty() && email.isNotEmpty()){
            Toast.makeText(this, "t", Toast.LENGTH_SHORT).show()
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:") // only email apps should handle this

         
            intent.putExtra(Intent.EXTRA_EMAIL, email)
            intent.putExtra(Intent.EXTRA_SUBJECT, "Zotes Poke Order")
            intent.putExtra(Intent.EXTRA_TEXT, "Here is your oder Number. Thank you for shopping");
            sqlCartHelper.deletePokemonExist()
            //pokemon.clear()
            adapter.notifyDataSetChanged()
            startActivity(intent)
            if (intent.resolveActivity(packageManager) != null) {


            }else{
                Toast.makeText(this, "null", Toast.LENGTH_SHORT).show()
            }

        }

//        val emailIntent = Intent(
//            Intent.ACTION_SENDTO,
//            fromParts("mailto", "edgar3ac@gmail.com", "null")
//        )
//
//        startActivity(Intent.createChooser(emailIntent, "Send email..."))

    }

    private fun getEmail():String{
        val userName = Firebase.auth.currentUser
        var currentUserName = ""
        userName?.let {
            for (profile in it.providerData) {
                // Id of the provider (ex: google.com)
                currentUserName = profile.email.toString()
            }
        }
        return currentUserName
    }
}