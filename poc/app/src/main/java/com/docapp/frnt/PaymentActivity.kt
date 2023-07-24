package com.docapp.frnt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.razorpay.Checkout
import com.razorpay.CheckoutActivity
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class PaymentActivity : AppCompatActivity(), PaymentResultListener {
    private lateinit var payload: JSONObject
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        payload = JSONObject(intent.getStringExtra("payload")!!)
        Checkout.preload(this)
        Checkout.sdkCheckIntegration(this)
        val intentCheckout = Checkout()
        CheckoutActivity()
        intentCheckout.setKeyID("rzp_test_rBkPB02vxnpDPW")
        intentCheckout.open(this, payload)
    }


    override fun onPaymentSuccess(p0: String?) {
        setResult(RESULT_OK)
        finish()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        setResult(RESULT_CANCELED)
        finish()
    }

}