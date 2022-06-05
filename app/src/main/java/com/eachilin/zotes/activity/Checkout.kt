package com.eachilin.zotes.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eachilin.zotes.DBhelper.CartHelper
import com.eachilin.zotes.adapter.CheckoutAdapter
import com.eachilin.zotes.databinding.ActivityCheckoutBinding
import com.eachilin.zotes.googlepay.PaymentsUtil
import com.eachilin.zotes.modal.CartItemModal
import com.eachilin.zotes.modal.CartModal
import com.eachilin.zotes.modal.OrderItemsModal
import com.eachilin.zotes.modal.OrderModal
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

private const val TAG = "Checkout"
class Checkout : AppCompatActivity() {


    private lateinit var cartListener: ListenerRegistration
    private var count by Delegates.notNull<Double>()
    private lateinit var binding : ActivityCheckoutBinding

    private lateinit var etName: EditText
    private lateinit var etAddress: EditText
    private lateinit var etEmail: EditText

    private lateinit var sqlCartHelper: CartHelper

    private var productOrder = ArrayList<CartItemModal>()
    private var orderItems = mutableListOf<OrderItemsModal>()

    private lateinit var rvCheckout:RecyclerView
    private lateinit var firestoreDB:FirebaseFirestore
    private val adapter = CheckoutAdapter(productOrder)


    private val SHIPPING_COST_CENTS = 9 * PaymentsUtil.CENTS.toLong()
    private lateinit var paymentsClient: PaymentsClient
    private val LOAD_PAYMENT_DATA_REQUEST_CODE = 991





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firestoreDB = FirebaseFirestore.getInstance()
        sqlCartHelper = CartHelper(this)

        rvCheckout = binding.rvCheck
        rvCheckout.adapter = adapter
        rvCheckout.layoutManager = LinearLayoutManager(this)
        fetchData()

        etName = binding.editTextTextPersonName
        etAddress = binding.etAddress
        etEmail = binding.etEmail
//        btnBuy = binding.btncBuyNow

        paymentsClient = PaymentsUtil.createPaymentsClient(this)
        possiblyShowGooglePayButton()

        binding.btncBuyNow.root.setOnClickListener {
            completeOrder()
            requestPayment()
        }


    }

    private fun goToMain() {
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
///

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
            binding.btncBuyNow.root.visibility = View.VISIBLE
        } else {
            Toast.makeText(
                this,
                "Unfortunately, Google Pay is not available on this device",
                Toast.LENGTH_LONG).show();
        }
    }

    private fun requestPayment() {

        // Disables the button to prevent multiple clicks.
        binding.btncBuyNow.root.isClickable = false

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.
        val garmentPrice = count.toDouble()
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
                paymentsClient.loadPaymentData(request), this, LOAD_PAYMENT_DATA_REQUEST_CODE)
        }

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            // Value passed in AutoResolveHelper
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    RESULT_OK ->
                        data?.let { intent ->
                            PaymentData.getFromIntent(intent)?.let(::handlePaymentSuccess)
                        }

                    RESULT_CANCELED -> {
                        // The user cancelled the payment attempt
                    }

                    AutoResolveHelper.RESULT_ERROR -> {
                        AutoResolveHelper.getStatusFromIntent(data)?.let {
                            handleError(it.statusCode)
                        }
                    }
                }

                // Re-enables the Google Pay payment button.
                binding.btncBuyNow.root.isClickable = true
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

            Toast.makeText(this, "test", Toast.LENGTH_LONG).show()

            // Logging token string.
            Log.d("GooglePaymentToken", paymentMethodData
                .getJSONObject("tokenizationData")
                .getString("token"))
            deleteCollection()
            goToMain()

        } catch (e: JSONException) {
            Log.e("handlePaymentSuccess", "Error: " + e.toString())
        }

    }

    private fun deleteCollection() {
        for(item in productOrder){
            var items = firestoreDB.collection("zotesOrderCart").document(item.id.toString()).delete()
            items.addOnCompleteListener {
                Log.e(TAG, "deleted document ${item.id}")
            }
        }


    }

    private fun handleError(statusCode: Int) {
        Log.w("loadPaymentData failed", String.format("Error code: %d", statusCode))
    }


    ///

    private fun fetchData() {
        val email = getEmail()
        val cartQuery =  firestoreDB.collection("zotesOrderCart").whereEqualTo("user.username", email)
        cartListener =  cartQuery.addSnapshotListener { snapshot, exception ->
            if(exception != null || snapshot == null){
                Log.e(TAG, "exception occurred", exception)
                return@addSnapshotListener
            }
            productOrder.clear()
            for (dc: DocumentChange in snapshot?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {

                    val orderItem: CartItemModal = dc.document.toObject(CartItemModal::class.java)
                    productOrder.add(orderItem)
                }
            }
            adapter.notifyDataSetChanged()
            countTotalVal()

        }

    }



    private fun countTotalVal(){
         count =0.0
        if(productOrder.isEmpty()){
            count =0.0
        }else{
            count = 0.0
            for(item in productOrder){
                count += item.price.times(item.count)
            }
        }
        val finalcount = BigDecimal(count).setScale(2, RoundingMode.HALF_EVEN)
        count = finalcount.toDouble()
        binding.tvcCost.text = "$ $count"
    }

    @SuppressLint("SimpleDateFormat")
    private fun completeOrder(){
        val email =getEmail()
        for(orderInformation in productOrder){
            var itemID = orderInformation.itemId
            var count = orderInformation.count
            var imageLink = orderInformation.image

            var cost = orderInformation.count.times(orderInformation.price)

            var newOrderItem = OrderItemsModal(orderInformation.name.toString(), imageLink,
                cost?.toLong(), count)
            orderItems.add(newOrderItem)
        }

        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = sdf.format(Date())
        var newOrder = OrderModal("", currentDate,  count, email, orderItems  )
        firestoreDB.collection("zotesCompletedOrder").add(newOrder)
            .addOnCompleteListener { newZotesOrder->
                if(newZotesOrder.isSuccessful){
                    Log.e(TAG,"uploaded successfully")
                    Toast.makeText(this, "uploadeded", Toast.LENGTH_SHORT).show()
                    checkout()

                }else{
                    Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()

                }
            }


    }

    private fun checkout() {
        val name = etName.text.trim().toString()
        val address = etAddress.text.trim().toString()
        val email = etEmail.text.trim().toString()

        if(name.isNotEmpty() && address.isNotEmpty() && email.isNotEmpty()){
            Toast.makeText(this, "t", Toast.LENGTH_SHORT).show()
//            val intent = Intent(Intent.ACTION_SENDTO)
//            intent.data = Uri.parse("mailto:") // only email apps should handle this
//
//
//            intent.putExtra(Intent.EXTRA_EMAIL, email)
//            intent.putExtra(Intent.EXTRA_SUBJECT, "Zotes Poke Order")
//            intent.putExtra(Intent.EXTRA_TEXT, "Here is your oder Number. Thank you for shopping");
            sqlCartHelper.deletePokemonExist()
            //pokemon.clear()
            adapter.notifyDataSetChanged()
//            startActivity(intent)
//            if (intent.resolveActivity(packageManager) != null) {
//
//
//            }else{
//                Toast.makeText(this, "null", Toast.LENGTH_SHORT).show()
//            }

        }

//        val emailIntent = Intent(
//            Intent.ACTION_SENDTO,
//            fromParts("mailto", "edgar3ac@gmail.com", "null")
//        )
//
//        startActivity(Intent.createChooser(emailIntent, "Send email..."))

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