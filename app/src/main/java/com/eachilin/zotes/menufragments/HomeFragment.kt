package com.eachilin.zotes.menufragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eachilin.zotes.R
import com.eachilin.zotes.adapter.BusinessAdapter
import com.eachilin.zotes.api.BusinessSearchResultItem
import com.eachilin.zotes.api.BusinessService
import com.eachilin.zotes.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.eachilin.zotes.ItemOffsetDecoration


private const val TAG = "HomeFragment"
private const val BASE_URL = "https://fakestoreapi.com/"
class HomeFragment : Fragment() {

    private lateinit var service: Unit
    private var _binding : FragmentHomeBinding? = null
    private val  binding get() = _binding!!

    private val businessInfo = mutableListOf<BusinessSearchResultItem>()
    private val  adapter = BusinessAdapter( businessInfo)
    private  lateinit var  rvBussiness : RecyclerView




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        rvBussiness  = binding.rvRestaurants

        rvBussiness.adapter = adapter
        rvBussiness.layoutManager = GridLayoutManager(context, 2)
        val itemDecoration = ItemOffsetDecoration(context!!, R.dimen.item_offset)
        rvBussiness.addItemDecoration(itemDecoration)

        if(businessInfo.size == 0){
            fetchData()
        }


        setHasOptionsMenu(true)

        binding.etSearchDetails.doAfterTextChanged { task ->

            val temp = mutableListOf<BusinessSearchResultItem>()

            if(task?.isNotEmpty() == true){
                var text = task.toString().lowercase()
                for (item in businessInfo){
                    val title = item.title.lowercase()
                    if(title.contains(text)){
                        temp.add(item)
                    }
                }
            }
            if (task != null) {
                if (task.isEmpty()) {
                    rvBussiness.adapter = BusinessAdapter(businessInfo)
                } else {
                    rvBussiness.adapter = BusinessAdapter(temp)

                }
            }
        }

    }



    private fun fetchData() {
        businessInfo.clear()
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        val businessService = retrofit.create(BusinessService::class.java)
        service = businessService.businessInfo("20")
                .enqueue(object : Callback<List<BusinessSearchResultItem>> {
                    override fun onResponse(
                        call: Call<List<BusinessSearchResultItem>>,
                        response: Response<List<BusinessSearchResultItem>>,
                    ) {
                        Log.i(TAG, "onResponse $response")
                        val body = response.body()
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
//            var desc = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."
//            var image = "https://hips.hearstapps.com/vader-prod.s3.amazonaws.com/1566927496-button-down-4-1566927485.jpg"
//            var rating = Rating(432,3.4)
//            var tempItem = BusinessSearchResultItem("Mens Clothing", "Just a million dollar moon  $desc", 23, image, 100.32, rating, "Uchiha mantle" )
//        businessInfo.add(tempItem)
//        adapter.notifyDataSetChanged()

    }

    override fun onStop() {
        super.onStop()

    }



}