package com.eachilin.zotes.menufragments

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
import com.eachilin.zotes.Checkout
import com.eachilin.zotes.DBhelper.CartHelper
import com.eachilin.zotes.PokeId
import com.eachilin.zotes.R
import com.eachilin.zotes.adapter.PokeCartAdapter
import com.eachilin.zotes.databinding.FragmentCartBinding
import com.eachilin.zotes.modal.CartModal


private const val TAG = "CartFragment"
private const val BASE_URL = "https://pokeapi.co/api/v2/pokemon/"

class CartFragment : Fragment(), PokeId {
    private var _binding : FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var btnBuyNow:Button
    private lateinit var tvFinalPrice : TextView
    private lateinit var sqlCartHelper: CartHelper
    private var pokemonInfo = ArrayList<CartModal>()
    private var count: Int = 0
    private val adapter = PokeCartAdapter(pokemonInfo, ::onItemDeleteClick, ::onIncrementPrice, ::onDecrementPrice)
    private lateinit var rvShopping :RecyclerView

    fun onItemDeleteClick(poke:CartModal){
        Log.e(TAG, "selecte ${poke.id}")
        sqlCartHelper.deleteStudentById(poke.id)
        fetchData()
        countTotalVal()
    }

    fun onIncrementPrice(increasePrice: Int){
        count += increasePrice
        tvFinalPrice.text = count.toString()
    }

    fun onDecrementPrice(increasePrice: Int){
        count -= increasePrice
        tvFinalPrice.text = count.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter.setOnClickItem { Toast.makeText(context, "call back ${it.name}", Toast.LENGTH_SHORT).show() }
        adapter.setOnClickDeleteItem {Toast.makeText(context, "call back ${it.name}", Toast.LENGTH_SHORT).show()  }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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
        countTotalVal()


    }

    private fun openCheckout() {
        val intent = Intent(activity, Checkout::class.java)
        intent.putExtra("pokeList", pokemonInfo);
        startActivity(intent)
    }

    private fun fetchData() {
        pokemonInfo.clear()
        val pokeList = sqlCartHelper.getAllPokemon()
        Log.i( TAG, "${pokeList.size}")
        pokemonInfo.addAll(pokeList)
        adapter.notifyDataSetChanged()

    }
    private fun countTotalVal(){
        if(pokemonInfo.isEmpty()){
            count =0
        }else{
            count = 0
            for(item in pokemonInfo){
                var price = item.pokeID?.toInt()?.times(15)
                if (price != null) {
                    count += price
                }
            }
        }
        tvFinalPrice.text = count.toString()
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)

        val search : MenuItem? = menu.findItem(R.id.nav_search)
        val searchView : SearchView = search?.actionView as SearchView
        searchView.queryHint = "Search Cart"

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                val temp = ArrayList<CartModal>()
                if(text?.isNotEmpty() == true){
                    for (poke in pokemonInfo){
                        if(poke.name?.contains(text) == true){
                            temp.add(poke)
                        }
                    }
                }
                if (text != null) {
                    if(text.isEmpty()){
                        rvShopping.adapter = PokeCartAdapter(
                            pokemonInfo,
                            ::onItemDeleteClick,
                            ::onIncrementPrice,
                            ::onDecrementPrice
                        )
                    }else{
                        rvShopping.adapter = PokeCartAdapter(
                            temp,
                            ::onItemDeleteClick,
                            ::onIncrementPrice,
                            ::onDecrementPrice
                        )

                    }
                }

                return true
            }

        })


        return super.onCreateOptionsMenu(menu, inflater)

    }



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {

            }
    }

    override fun onAdapterRemoveItemClickListener(poke: CartModal) {
        sqlCartHelper.deleteStudentById(poke.id)
        adapter.notifyDataSetChanged()
    }
}