package com.eachilin.zotes.menufragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SearchView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eachilin.zotes.R
import com.eachilin.zotes.adapter.BusinessAdapter
import com.eachilin.zotes.api.BusinessSearchResult
import com.eachilin.zotes.api.BusinessSearchResultItem
import com.eachilin.zotes.api.BusinessService
import com.eachilin.zotes.databinding.FragmentHomeBinding
import com.eachilin.zotes.pokemon.PokemonInitialData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "HomeFragment"
private const val BASE_URL = "https://fakestoreapi.com/"
class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val  binding get() = _binding!!

    private val businessInfo = mutableListOf<BusinessSearchResultItem>()
    private val  adapter = BusinessAdapter( businessInfo)
    private  lateinit var  rvBussiness : RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        rvBussiness  = binding.rvRestaurants

        rvBussiness.adapter = adapter
        rvBussiness.layoutManager = GridLayoutManager(context, 2)


        if(businessInfo.size == 0){
            fetchData()
        }


        setHasOptionsMenu(true)

        binding.etSearchDetails.doAfterTextChanged { task ->
            var text= task

            val temp = mutableListOf<BusinessSearchResultItem>()
            if(text?.isNotEmpty() == true){
                for (item in businessInfo){
                    if(item.title.contains(text)){
                        temp.add(item)
                    }
                }
            }
            if (text != null) {
                if(text.isEmpty()){
                    rvBussiness.adapter = BusinessAdapter( businessInfo)
                }else{
                    rvBussiness.adapter = BusinessAdapter( temp)

                }
            }
        }

    }



    private fun fetchData() {
        businessInfo.clear()
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        var businessService = retrofit.create(BusinessService::class.java)
            businessService.BusinessInfo("20")
                .enqueue(object : Callback<List<BusinessSearchResultItem>> {
                    override fun onResponse(
                        call: Call<List<BusinessSearchResultItem>>,
                        response: Response<List<BusinessSearchResultItem>>,
                    ) {
                        Log.i(TAG, "onResponse $response")
                        var body = response.body()
                        if(body == null){
                            Log.w(TAG, "Did not receive valid response body from Yelp API... exiting")
                            return
                         }
                        businessInfo.addAll(body)
                        adapter.notifyDataSetChanged()
                    }

                    override fun onFailure(
                        call: Call<List<BusinessSearchResultItem>>,
                        t: Throwable,
                    ) {
                        Log.i(TAG, "onFailure $t")
                    }

                })

//        val businessService = retrofit.create(BusinessService::class.java)
//        businessService.BusinessInfo("5")
//            .enqueue(object : Callback<BusinessSearchResult> {
//                override fun onResponse(call: Call<BusinessSearchResult>, response: Response<BusinessSearchResult>) {
//                    Log.i(TAG, "onResponse $response")
//                    val body = response.body()
//                    if(body == null){
//                        Log.w(TAG, "Did not receive valid response body from Yelp API... exiting")
//                        return
//                    }
//
//                    Log.e(TAG, "${body.products}")
//                    businessInfo.addAll(body.products)
//                    adapter?.notifyDataSetChanged()
//                }
//
//                override fun onFailure(call: Call<BusinessSearchResult>, t: Throwable) {
//                    Log.e(TAG, "onFailure $t")
//                }
//
//            })

    }



}