package com.eachilin.zotes

import android.content.Intent
import android.net.Uri.fromParts
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private var pokemon = ArrayList<CartModal>()

    private lateinit var rvCheckout:RecyclerView
    private val adapter = CheckoutAdapter(pokemon)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        rvCheckout = binding.rvCheck
        rvCheckout.adapter = adapter
        rvCheckout.layoutManager = LinearLayoutManager(this)
        fetchData()

        etName = binding.editTextTextPersonName
        etAddress = binding.etAddress
        etEmail = binding.etEmail
        btnBuy = binding.btncBuyNow

        btnBuy.setOnClickListener { checkout() }

    }

    private fun fetchData() {
        val myList: ArrayList<CartModal>? = intent.getSerializableExtra("pokeList") as ArrayList<CartModal>?
        Log.e(TAG, myList.toString())

            pokemon.addAll(myList!!)
            adapter?.notifyDataSetChanged()


    }

    private fun checkout() {
        val name = etName.text
        val address = etAddress.text
        val emailT = etEmail.text

//        val emailIntent = Intent(
//            Intent.ACTION_SENDTO,
//            fromParts("mailto", "edgar3ac@gmail.com", "null")
//        )
//
//        startActivity(Intent.createChooser(emailIntent, "Send email..."))


    }
}