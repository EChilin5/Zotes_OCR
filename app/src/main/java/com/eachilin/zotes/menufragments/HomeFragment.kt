package com.eachilin.zotes.menufragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.SearchView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eachilin.zotes.R
import com.eachilin.zotes.RestaurantsAdapter
import com.eachilin.zotes.databinding.FragmentHomeBinding
import com.eachilin.zotes.pokemon.PokemSearchResult
import com.eachilin.zotes.pokemon.PokemonInitialData
import com.eachilin.zotes.pokemon.PokemonService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "HomeFragment"
private const val BASE_URL = "https://pokeapi.co/api/v2/pokemon/"
class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val  binding get() = _binding!!

    private val restaurant = mutableListOf<PokemonInitialData>()
    private val  adapter = RestaurantsAdapter( restaurant)
    private  lateinit var  rvRestaurant : RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        rvRestaurant  = binding.rvRestaurants

        rvRestaurant.adapter = adapter
        rvRestaurant.layoutManager = GridLayoutManager(context, 2)
        if(restaurant.size == 0){
            fetchData()
        }

//        val pokeHome : ImageView = binding.ivPokeHome
//
//        Glide.with(this)
//            .load(R.drawable.pokemon_1)
//            .override(300, 300)
//            .into(pokeHome)

        setHasOptionsMenu(true)

        binding.etSearchDetails.doAfterTextChanged { task ->
            var text= task

            val temp = mutableListOf<PokemonInitialData>()
            if(text?.isNotEmpty() == true){
                for (poke in restaurant){
                    if(poke.name.contains(text)){
                        temp.add(poke)
                    }
                }
            }
            if (text != null) {
                if(text.isEmpty()){
                    rvRestaurant.adapter = RestaurantsAdapter( restaurant)
                }else{
                    rvRestaurant.adapter = RestaurantsAdapter( temp)

                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)

        val search : MenuItem? = menu?.findItem(R.id.nav_search)
        val searchView : SearchView = search?.actionView as SearchView
        searchView.queryHint = "Search Something"

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                val temp = mutableListOf<PokemonInitialData>()
               if(text?.isNotEmpty() == true){
                   for (poke in restaurant){
                        if(poke.name.contains(text)){
                            temp.add(poke)
                        }
                   }
               }
                if (text != null) {
                    if(text.isEmpty()){
                        rvRestaurant.adapter = RestaurantsAdapter( restaurant)
                    }else{
                        rvRestaurant.adapter = RestaurantsAdapter( temp)

                    }
                }

                return true
            }

        })


        return super.onCreateOptionsMenu(menu, inflater)

    }

    private fun fetchData() {
        restaurant.clear()
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        val yelpService = retrofit.create(PokemonService::class.java)
        yelpService.PokemonInfo("0", "151")
            .enqueue(object : Callback<PokemSearchResult> {
                override fun onResponse(call: Call<PokemSearchResult>, response: Response<PokemSearchResult>) {
                    Log.i(TAG, "onResponse $response")
                    val body = response.body()
                    if(body == null){
                        Log.w(TAG, "Did not receive valid response body from Yelp API... exiting")
                        return
                    }
                    restaurant.addAll(body.result)
                    adapter?.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<PokemSearchResult>, t: Throwable) {
                    Log.i(TAG, "onFailuer $t")
                }

            })

    }



}