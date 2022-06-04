package com.eachilin.zotes.overlay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.eachilin.zotes.databinding.FragmentReviewItemOverlayBinding
import com.eachilin.zotes.modal.ReviewModal
import com.eachilin.zotes.modal.UserModal
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class ReviewItemOverlay : DialogFragment() {

    private lateinit var _binding : FragmentReviewItemOverlayBinding
    private val binding get() = _binding

    private lateinit var firestoreDB : FirebaseFirestore

    private lateinit var btnAddItem : Button
    private lateinit var etName : EditText
    private lateinit var etRating : EditText
    private lateinit var etDescription: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        dialog?.getWindow()?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentReviewItemOverlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        var id : String = arguments?.get("id") as String

        btnAddItem.setOnClickListener {
            addItemReview(id)
            this.dismiss()
        }

    }

    private fun addItemReview(id: String) {
        val userName = Firebase.auth.currentUser
        var currentUserName = userName?.email.toString()


        val user = UserModal("", currentUserName)
        val review = ReviewModal("", etName.text.toString(), etRating.text.toString(), etDescription.text.toString(), id, System.currentTimeMillis(), user)

        firestoreDB.collection("zotesReviewPost").add(review)
            .addOnCompleteListener { reviewCreation ->
                if(reviewCreation.isSuccessful){
                   // Toast.makeText(activity?.applicationContext, "sucess", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, "sucess", Toast.LENGTH_SHORT).show()
                }

        }
    }

    private fun initView() {
        btnAddItem = binding.btnCreateReview
        etName = binding.etReviewerName
        etDescription = binding.etReviewDescription
        etRating = binding.etReviewRating

        firestoreDB = FirebaseFirestore.getInstance()
    }



}