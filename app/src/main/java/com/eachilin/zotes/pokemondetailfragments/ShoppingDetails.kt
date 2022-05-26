package com.eachilin.zotes.pokemondetailfragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eachilin.zotes.overlay.ReviewItemOverlay
import com.eachilin.zotes.adapter.ReviewAdapter
import com.eachilin.zotes.databinding.FragmentShoppingDetailsBinding
import com.eachilin.zotes.modal.ReviewModal
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


private const val TAG = "ShoppingDetails"
class ShoppingDetails(var name: String, var id: String) : Fragment() {



    private var _binding : FragmentShoppingDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestoreDB : FirebaseFirestore

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

        fetchData()
    }

    private fun openAddNewItem() {
       val dialog = ReviewItemOverlay()
        val fm = activity?.supportFragmentManager
        if(fm != null){
            dialog.show(fm, "name")
        }
    }

    private fun initView() {
        Log.e(TAG, this.name)
        btnflAddReview = binding.flBtnReview
        rvItemReview = binding.rvItemReview

        reviewPost = mutableListOf()
        adapter = ReviewAdapter(reviewPost)
        rvItemReview.adapter = adapter
        rvItemReview.layoutManager = LinearLayoutManager(context)

        firestoreDB = FirebaseFirestore.getInstance()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchData(){
        var reviewPostReference = firestoreDB.collection("zotesReviewPost")
            .orderBy("creation_time_ms", Query.Direction.DESCENDING )
        reviewPostReference = reviewPostReference.whereEqualTo("pokemonID", this.id)
        reviewPostReference.addSnapshotListener { snapshot, exception ->
            if(exception != null || snapshot == null){
                Log.e(TAG, "exception occurred", exception)
                return@addSnapshotListener
            }

            for (dc: DocumentChange in snapshot?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {

                    val reviewItem: ReviewModal = dc.document.toObject(ReviewModal::class.java)
                    reviewPost.add(reviewItem)
                }
            }
            adapter.notifyDataSetChanged()
        }
    }




}