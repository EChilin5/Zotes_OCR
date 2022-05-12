package com.eachilin.zotes

import android.content.Intent
import android.net.Uri
import android.net.Uri.fromParts
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
import com.eachilin.zotes.adapter.PokeCartAdapter
import com.eachilin.zotes.databinding.ActivityCheckoutBinding
import com.eachilin.zotes.databinding.ActivityMainBinding
import com.eachilin.zotes.databinding.FragmentCartBinding
import com.eachilin.zotes.email.EmailService
import com.eachilin.zotes.modal.CartModal
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URI
import java.util.ArrayList
import javax.mail.internet.InternetAddress

private const val TAG = "Checkout"
class Checkout : AppCompatActivity() {

    private lateinit var binding : ActivityCheckoutBinding

    private lateinit var etName: EditText
    private lateinit var etAddress: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnBuy: Button
    private lateinit var sqlCartHelper: CartHelper

    private var pokemon = ArrayList<CartModal>()

    private lateinit var rvCheckout:RecyclerView
    private val adapter = CheckoutAdapter(pokemon)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sqlCartHelper = this?.let { CartHelper(it) }!!


        rvCheckout = binding.rvCheck
        rvCheckout.adapter = adapter
        rvCheckout.layoutManager = LinearLayoutManager(this)
        fetchData()

        etName = binding.editTextTextPersonName
        etAddress = binding.etAddress
        etEmail = binding.etEmail
        btnBuy = binding.btncBuyNow

        btnBuy.setOnClickListener { checkout() }
        countTotalVal()

    }

    private fun fetchData() {
        val myList: ArrayList<CartModal>? = intent.getSerializableExtra("pokeList") as ArrayList<CartModal>?
        Log.e(TAG, myList.toString())

            pokemon.addAll(myList!!)
            adapter?.notifyDataSetChanged()


    }

    private fun countTotalVal(){
        var count =0
        if(pokemon.isEmpty()){
            count =0
        }else{
            count = 0
            for(item in pokemon){
                var price = item.pokeID?.toInt()?.times(15)
                price = price!!.times(item.count)
                if (price != null) {
                    count += price
                }
            }
        }
        binding.tvcCost.text = "$ $count"
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
            pokemon.clear()
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
}