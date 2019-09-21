package org.kabiri.android.finance.firebase

import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import org.kabiri.android.finance.MainActivity
import org.kabiri.android.finance.R
import org.kabiri.android.finance.model.PaymentEntry

class RealtimeDatabaseHelper {

    fun initFBase(activity: AppCompatActivity) {
        // initialize fireBaseApp
        FirebaseApp.initializeApp(activity)
        // log into fireBase
        val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
        // firebase login ui - opens if needed.
        activity.startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(),
            MainActivity.RC_SIGN_IN
        )
    }

    fun pushPaymentEntry(activity: AppCompatActivity, user: FirebaseUser, paymentEntry: PaymentEntry, callback: Callback) {
        val db = FirebaseDatabase.getInstance()
        val paymentRef = db.getReference("payments/${user.uid}")
        paymentRef.push().setValue(paymentEntry) { error, _ ->
            if (error != null) {
                Log.e(TAG, error.message)
                callback.onFailure()
            }
            else {
                Toast.makeText(activity, R.string.main_activity_entry_added, Toast.LENGTH_SHORT).show()
                Log.i(TAG, "payment entry added!")
                callback.onSuccess()
            }
        }
    }

    interface Callback {
        fun onSuccess()
        fun onFailure()
    }

    companion object {
        const val TAG = "RealtimeDatabaseHelper"
    }
}