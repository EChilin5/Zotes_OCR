package com.eachilin.zotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.eachilin.zotes.databinding.FragmentReviewItemOverlayBinding


class ReviewItemOverlay : DialogFragment() {

    private lateinit var _binding : FragmentReviewItemOverlayBinding
    private val binding get() = _binding!!

    private lateinit var btnAddItem : Button
    private lateinit var etName : EditText
    private lateinit var etRating : EditText
    private lateinit var etDescription: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        btnAddItem.setOnClickListener {
            this.dismiss()
        }

    }

    private fun initView() {
        btnAddItem = binding.btnCreateReview
        etName = binding.etReviewerName
        etDescription = binding.etReviewDescription
        etRating = binding.etReviewRating
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReviewItemOverlay().apply {
                arguments = Bundle().apply {

                }
            }
    }
}