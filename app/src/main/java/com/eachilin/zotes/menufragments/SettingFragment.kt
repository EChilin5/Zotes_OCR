package com.eachilin.zotes.menufragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.eachilin.zotes.activity.LoginActivity
import com.eachilin.zotes.databinding.FragmentSettingBinding
import com.eachilin.zotes.overlay.ForgotPasswordOverlay
import com.google.firebase.auth.FirebaseAuth


private const val TAG = "SettingFragment"
class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var btnSignOut: Button
    private lateinit var etReset :TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        btnSignOut.setOnClickListener {
            Log.i(TAG, "User wants to logout")
            FirebaseAuth.getInstance().signOut()
            val intent  = Intent(context, LoginActivity::class.java)
            context?.startActivity(intent)
            activity?.finish()

        }

        etReset.setOnClickListener {
            openResetPassword()
        }

    }

    private fun openResetPassword() {

        val dialog = ForgotPasswordOverlay()
        val fm = activity?.supportFragmentManager
        if (fm != null) {
            dialog.show(fm, "name")
        }
    }

    private fun initView() {
        btnSignOut = binding.btnSignOut
        etReset = binding.tvResetPassword
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}