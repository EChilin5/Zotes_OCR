package com.eachilin.zotes.overlay

import android.app.ActionBar
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.eachilin.zotes.activity.MainActivity

import com.eachilin.zotes.databinding.FragmentOverlayBuyNowBinding
import com.eachilin.zotes.googlepay.PaymentsUtil
import com.eachilin.zotes.modal.OrderItemsModal
import com.eachilin.zotes.modal.OrderModal
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

private const val TAG ="overlay_buy_now"
class overlay_buy_now : DialogFragment() {

    private lateinit var btnBuy: RelativeLayout
    private lateinit var cost: String
    private var _binding: FragmentOverlayBuyNowBinding? = null
    private val binding get() = _binding!!
    private var orderItems = mutableListOf<OrderItemsModal>()


    private val SHIPPING_COST_CENTS = 9 * PaymentsUtil.CENTS.toLong()
    private lateinit var paymentsClient: PaymentsClient
    private val LOAD_PAYMENT_DATA_REQUEST_CODE = 991

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
        cost = requireArguments().getString("cost")!!

        var tvName = binding.tvdfPokeName
        var tvCost = binding.tvdfCost
        btnBuy = binding.btnFinalBuy.root
        var ivDisply = binding.imageView3

        tvName.text = getTitle(name)
        tvCost.text = cost
        paymentsClient = PaymentsUtil.createPaymentsClient(context as Activity)
        btnBuy.setOnClickListener {
            if(tvName.text.isNotEmpty() && tvCost.text.isNotEmpty()){
                completeOrder(tvName.text.toString(), 1, imageLink!!, cost.toDouble() )
                dismiss()
                requestPayment()
            }
            else{
                return@setOnClickListener
            }
        }

        Glide.with(this)
            .load(imageLink)
            .centerInside()
//                .override(, 100)
            .into(ivDisply)

    }

    private fun completeOrder(name:String, count : Int, imageLink:String, cost:Double){
        val email =getEmail()
        var firestoreDB = FirebaseFirestore.getInstance()
//
//            var count = orderInformation.count
//            var imageLink = orderInformation.image
//
//            var cost = orderInformation.count.times(orderInformation.price)

            var newOrderItem = OrderItemsModal(name, imageLink,
                cost?.toLong(), count)
        orderItems.add(newOrderItem)



        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = sdf.format(Date())
        var newOrder = OrderModal("", currentDate,  cost.toDouble(), email, orderItems  )
        firestoreDB.collection("zotesCompletedOrder").add(newOrder)
            .addOnCompleteListener { newZotesOrder->
                if(newZotesOrder.isSuccessful){
                    Log.e(TAG,"uploaded successfully")
//                    Toast.makeText(this, "uploadeded", Toast.LENGTH_SHORT).show()

                }else{
                    Log.e(TAG,"uploaded FAILED")

//                    Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()

                }
            }


    }

    private fun getTitle(title: String?):String {
        var seperate = " "
        val strList = title?.split(seperate)
        var word = ""
        var count = 0;
        for(item in strList!!){
            word += "$item "
            count++
            if(count == 5){
                word = word.trim() + "..."
                break
            }

        }
        return word
    }



    private fun possiblyShowGooglePayButton() {

        val isReadyToPayJson = PaymentsUtil.isReadyToPayRequest() ?: return
        val request = IsReadyToPayRequest.fromJson(isReadyToPayJson.toString()) ?: return

        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        val task = paymentsClient.isReadyToPay(request)
        task.addOnCompleteListener { completedTask ->
            try {
                completedTask.getResult(ApiException::class.java)?.let(::setGooglePayAvailable)
            } catch (exception: ApiException) {
                // Process error
                Log.w("isReadyToPay failed", exception)
            }
        }
    }

    private fun setGooglePayAvailable(available: Boolean) {
        if (available) {
            btnBuy.visibility = View.VISIBLE
        } else {
            Toast.makeText(
                context,
                "Unfortunately, Google Pay is not available on this device",
                Toast.LENGTH_LONG).show();
        }
    }

    private fun requestPayment() {

        // Disables the button to prevent multiple clicks.
        btnBuy.isClickable = false

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.
        val garmentPrice = cost.toDouble()
        val priceCents = Math.round(garmentPrice * PaymentsUtil.CENTS.toLong()) + SHIPPING_COST_CENTS

        val paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(priceCents)
        if (paymentDataRequestJson == null) {
            Log.e("RequestPayment", "Can't fetch payment data request")
            return
        }
        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString())

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        if (request != null) {
            AutoResolveHelper.resolveTask(
                paymentsClient.loadPaymentData(request),
                context as Activity, LOAD_PAYMENT_DATA_REQUEST_CODE)
        }

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            // Value passed in AutoResolveHelper
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    AppCompatActivity.RESULT_OK ->
                        data?.let { intent ->
                            PaymentData.getFromIntent(intent)?.let(::handlePaymentSuccess)
                        }

                    AppCompatActivity.RESULT_CANCELED -> {
                        // The user cancelled the payment attempt
                    }

                    AutoResolveHelper.RESULT_ERROR -> {
                        AutoResolveHelper.getStatusFromIntent(data)?.let {
                            handleError(it.statusCode)
                        }
                    }
                }

                // Re-enables the Google Pay payment button.
                btnBuy.isClickable = true
            }
        }
    }

    private fun handlePaymentSuccess(paymentData: PaymentData) {
        val paymentInformation = paymentData.toJson() ?: return

        try {
            // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
            val paymentMethodData = JSONObject(paymentInformation).getJSONObject("paymentMethodData")
            val billingName = paymentMethodData.getJSONObject("info")
                .getJSONObject("billingAddress").getString("name")
            Log.d("BillingName", billingName)

            Toast.makeText(context, "test", Toast.LENGTH_LONG).show()

            // Logging token string.
            Log.d("GooglePaymentToken", paymentMethodData
                .getJSONObject("tokenizationData")
                .getString("token"))
            dismiss()
            goToMain()

        } catch (e: JSONException) {
            Log.e("handlePaymentSuccess", "Error: " + e.toString())
        }

    }


    private fun handleError(statusCode: Int) {
        Log.w("loadPaymentData failed", String.format("Error code: %d", statusCode))
    }

      private fun goToMain() {
          var intent = Intent(context, MainActivity::class.java)
          context?.startActivity(intent)
    }

    private fun getEmail():String{
        val userName = Firebase.auth.currentUser
        var currentUserName = userName?.email
        return currentUserName.toString()
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