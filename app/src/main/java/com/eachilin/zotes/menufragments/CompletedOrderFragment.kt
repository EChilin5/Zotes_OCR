package com.eachilin.zotes.menufragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eachilin.zotes.adapter.OrderAdapter
import com.eachilin.zotes.databinding.FragmentCompletedOrderBinding
import com.eachilin.zotes.modal.OrderModal
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


private const val TAG ="CompletedOrderFragment"
class CompletedOrderFragment : Fragment() {

    private var _binding: FragmentCompletedOrderBinding? =null
    private val binding get() = _binding!!

    private var orderInfo = mutableListOf<OrderModal>()
    private lateinit var firestore: FirebaseFirestore
    private val adapter = OrderAdapter(orderInfo)

    private lateinit var rvOrder:RecyclerView

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCompletedOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        fetchData()
    }

    private fun fetchData() {
        val email = getEmail()
        val orderCartReference = firestore.collection("zotesCompletedOrder").whereEqualTo("userOrderName", email)
        orderCartReference.addSnapshotListener { snapshot, exception ->
            if(exception != null || snapshot == null){
                Log.e(TAG, "exception occurred", exception)
                return@addSnapshotListener
            }
            orderInfo.clear()
            for (dc: DocumentChange in snapshot.documentChanges) {
                if (dc.type == DocumentChange.Type.ADDED) {

                    val orderItem: OrderModal = dc.document.toObject(OrderModal::class.java)

                    orderInfo.add(orderItem)
                }
            }
            adapter.notifyDataSetChanged()

        }
    }

    private fun initView() {
        firestore = FirebaseFirestore.getInstance()
        rvOrder = binding.rvOrderInfo
        rvOrder.adapter = adapter
        rvOrder.layoutManager = LinearLayoutManager(context)
    }

    private fun getEmail():String{
        val userName = Firebase.auth.currentUser
        var currentUserName = ""
        userName?.let {
            for (profile in it.providerData) {
                // Id of the provider (ex: google.com)
                currentUserName = profile.email.toString()
            }
        }
        return currentUserName
    }

}