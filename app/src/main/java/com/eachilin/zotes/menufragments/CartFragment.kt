package com.eachilin.zotes.menufragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eachilin.zotes.DBhelper.CartHelper
import com.eachilin.zotes.PokeId
import com.eachilin.zotes.R
import com.eachilin.zotes.activity.Checkout
import com.eachilin.zotes.adapter.PokeCartAdapter
import com.eachilin.zotes.databinding.FragmentCartBinding
import com.eachilin.zotes.modal.CartItemModal
import com.eachilin.zotes.modal.CartModal
import com.eachilin.zotes.modal.OrderModal
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase


private const val TAG = "CartFragment"
private const val BASE_URL = "https://pokeapi.co/api/v2/pokemon/"

class CartFragment : Fragment(), PokeId {
    private lateinit var cartListener: ListenerRegistration
    private var _binding : FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var btnBuyNow:Button
    private lateinit var tvFinalPrice : TextView
    private lateinit var sqlCartHelper: CartHelper
    private var cartNewItems = ArrayList<CartItemModal>()
    private var count: Double = 0.0
    private val adapter = PokeCartAdapter(cartNewItems, ::onItemDeleteClick, ::onIncrementPrice, ::onDecrementPrice)
    private lateinit var rvShopping :RecyclerView
    private lateinit var firestore: FirebaseFirestore

    fun onItemDeleteClick(item:CartItemModal){
        Log.e(TAG, "selecte ${item.id}")
        firestore.collection("zotesOrderCart").document(item.id.toString()).delete().addOnCompleteListener {
            if(it.isSuccessful){
                Log.e(TAG, "deleted")
                cartNewItems.remove(item)
                adapter.notifyDataSetChanged()
            }
        }
//        sqlCartHelper.deleteStudentById(poke.id.t)
        //fetchData()
        countTotalVal()
    }

    fun onIncrementPrice(postId: Int, increasePrice: Double, countUpdate :Int, docId: String){
        count += increasePrice
        updateInformation(postId, countUpdate, docId)
        updateBadge(true)
    }

    fun onDecrementPrice(postId: Int, increasePrice: Double, countUpdate :Int, docId:String){
        count -= increasePrice
        updateInformation(postId, countUpdate, docId)
        updateBadge(false)
    }

    private fun updateInformation(postId: Int, countUpdate :Int, docId:String){
        cartListener.remove()
        updateFirebaseItemCount(docId, countUpdate)
        cartNewItems[postId].count = countUpdate
        tvFinalPrice.text = count.toString()
    }

    private fun updateFirebaseItemCount(id: String,  updateCount: Int, ){
        Log.e(TAG, "update count from cart ${updateCount}")
        firestore.collection("zotesOrderCart").document(id).update("count", updateCount).addOnCompleteListener {
            if(it.isSuccessful){
                Log.e(TAG, " updated")
            }
        }
    }

    fun updateBadge(increase :Boolean){
        val nav = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)

        val cartBadge = nav?.getOrCreateBadge(R.id.ic_cart)
        if(increase){
            cartBadge?.number = cartBadge?.number?.plus(1)!!
        }else{

            cartBadge?.number = cartBadge?.number?.minus(1)!!
        }
        cartBadge.isVisible = true

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter.setOnClickItem { Toast.makeText(context, "call back ${it.name}", Toast.LENGTH_SHORT).show() }
        adapter.setOnClickDeleteItem {Toast.makeText(context, "call back ${it.name}", Toast.LENGTH_SHORT).show()  }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Toast.makeText(context, "call back ", Toast.LENGTH_SHORT).show()
        adapter.setOnClickItem { Toast.makeText(context, "call back ${it.name}", Toast.LENGTH_SHORT).show() }
        adapter.setOnClickDeleteItem {Toast.makeText(context, "call back ${it.name}", Toast.LENGTH_SHORT).show()  }

        // Inflate the layout for this fragment
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sqlCartHelper = context?.let { CartHelper(it) }!!
        firestore = FirebaseFirestore.getInstance()
        rvShopping = binding.rvShoppingCart
        btnBuyNow = binding.btnCheckoutF
        tvFinalPrice = binding.tvFinalPrice

        btnBuyNow.setOnClickListener { openCheckout() }

        rvShopping.adapter = adapter
        rvShopping.layoutManager = LinearLayoutManager(context)

        adapter.setOnClickItem { Toast.makeText(context, "call back ${it.name}", Toast.LENGTH_SHORT).show() }
        adapter.setOnClickDeleteItem {Toast.makeText(context, "call back ${it.name}", Toast.LENGTH_SHORT).show()  }

        setHasOptionsMenu(true)

            fetchData()



    }

    private fun openCheckout() {
        val intent = Intent(activity, Checkout::class.java)
        intent.putExtra("pokeList", cartNewItems)
        startActivity(intent)
    }

    private fun fetchData() {
        cartNewItems.clear()
//        val pokeList = sqlCartHelper.getAllPokemon()
//        Log.i( TAG, "${pokeList.size}")
//        pokemonInfo.addAll(pokeList)
//        adapter.notifyDataSetChanged()
        val email = getEmail()
        Toast.makeText(context, email, Toast.LENGTH_SHORT).show()
       val cartQuery =  firestore.collection("zotesOrderCart").whereEqualTo("user.username", email)
       cartListener =  cartQuery.addSnapshotListener { snapshot, exception ->
            if(exception != null || snapshot == null){
                Log.e(TAG, "exception occurred", exception)
                return@addSnapshotListener
            }
           cartNewItems.clear()
            for (dc: DocumentChange in snapshot?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {

                    val orderItem: CartItemModal = dc.document.toObject(CartItemModal::class.java)
                    cartNewItems.add(orderItem)
                }
            }
            adapter.notifyDataSetChanged()
           countTotalVal()
        }

    }

    private fun getEmail(): String? {
        val userName = Firebase.auth.currentUser
        var currentUserName = userName?.email

        return currentUserName
    }

    @SuppressLint("SetTextI18n")
    private fun countTotalVal(){
        if(cartNewItems.isEmpty()){
            count =0.0
        }else{
            count = 0.0
            for(item in cartNewItems){
                count += item.price
            }
        }
        tvFinalPrice.text = "$ $count"
    }




    override fun onAdapterRemoveItemClickListener(poke: CartModal) {
        sqlCartHelper.deleteStudentById(poke.id)
        adapter.notifyDataSetChanged()
    }
}