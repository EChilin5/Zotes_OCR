package com.eachilin.zotes.pokemondetailfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eachilin.zotes.R
import com.eachilin.zotes.ReviewItemOverlay
import com.eachilin.zotes.adapter.ReviewAdapter
import com.eachilin.zotes.databinding.FragmentShoppingDetailsBinding
import com.eachilin.zotes.modal.ReviewModal
import com.google.android.material.floatingactionbutton.FloatingActionButton


private const val TAG = "ShoppingDetails"
class ShoppingDetails : Fragment() {

    private var _binding : FragmentShoppingDetailsBinding? = null
    private val binding get() = _binding!!

    // adapter
    private lateinit var reviewPost : MutableList<ReviewModal>
    private lateinit var adapter : ReviewAdapter

    // ui
    private lateinit var btnflAddReview : FloatingActionButton
    private lateinit var rvItemReview :RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentShoppingDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        btnflAddReview.setOnClickListener{
            openAddNewItem()
        }
    }

    private fun openAddNewItem() {
       val dialog = ReviewItemOverlay()
        val fm = activity?.supportFragmentManager
        if(fm != null){
            dialog.show(fm, "name")
        }
    }

    private fun initView() {
        btnflAddReview = binding.flBtnReview
        rvItemReview = binding.rvItemReview

        reviewPost = mutableListOf()
        adapter = ReviewAdapter(reviewPost)
        rvItemReview.adapter = adapter
        rvItemReview.layoutManager = LinearLayoutManager(context)
    }



    companion object {

        fun newInstance(param1: String, param2: String) =
            ShoppingDetails().apply {
                arguments = Bundle().apply {

                }
            }
    }
}