package com.eachilin.zotes.overlay

import android.app.ActionBar
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.eachilin.zotes.databinding.FragmentOverlayBuyNowBinding


class overlay_buy_now : DialogFragment() {

    private var _binding: FragmentOverlayBuyNowBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setGravity(Gravity.BOTTOM)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentOverlayBuyNowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageLink = requireArguments().getString("image")
        val name = requireArguments().getString("name")
        val cost = requireArguments().getString("cost")

        var tvName = binding.tvdfPokeName
        var tvCost = binding.tvdfCost
        var btnBuy = binding.btndfBuy
        var ivDisply = binding.imageView3

        tvName.text = name
        tvCost.text = cost

        Glide.with(this)
            .load(imageLink)
//                .override(, 100)
            .into(ivDisply)



    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment overlay_buy_now.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            overlay_buy_now().apply {
                arguments = Bundle().apply {

                }
            }
    }
}