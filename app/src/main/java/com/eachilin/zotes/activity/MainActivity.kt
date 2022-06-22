package com.eachilin.zotes.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.eachilin.zotes.DBhelper.CartHelper
import com.eachilin.zotes.R
import com.eachilin.zotes.databinding.ActivityMainBinding
import com.eachilin.zotes.menufragments.CartFragment
import com.eachilin.zotes.menufragments.CompletedOrderFragment
import com.eachilin.zotes.menufragments.HomeFragment
import com.eachilin.zotes.menufragments.SettingFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val homeFragment = HomeFragment()
    private val cartFragment = CartFragment()
    private val settingFragment = SettingFragment()
    private val orderFragment = CompletedOrderFragment()

    private lateinit var sqlCartHelper: CartHelper
    private lateinit var firestore: FirebaseFirestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sqlCartHelper = CartHelper(this)
        firestore = FirebaseFirestore.getInstance()

        val auth = FirebaseAuth.getInstance()
        val emailInfo = auth.currentUser?.email

        binding.bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.ic_home -> {

                    openFragment(homeFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.ic_cart -> {

                    openFragment(cartFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.ic_order -> {

                    openFragment(orderFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.ic_setting -> {

                    openFragment(settingFragment)
                    return@setOnItemSelectedListener true
                }

                else -> false
            }

        }

        val orderCartSize = firestore.collection("zotesOrderCart").whereEqualTo("user.username", emailInfo).get()
        orderCartSize.addOnCompleteListener {
            it.addOnCompleteListener {
                // it.result.size()
                val cartBadge = binding.bottomNav.getOrCreateBadge(R.id.ic_cart)
                cartBadge.isVisible = true
                cartBadge.number = it.result.size()
            }
        }




        openFragment(homeFragment)


    }

    private fun openFragment(fragment: Fragment){
        val transaction =supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }


}