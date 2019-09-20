package org.kabiri.android.finance

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import org.kabiri.android.finance.model.PaymentEntry
import org.kabiri.android.finance.viewmodel.MainActivityViewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private var user: FirebaseUser? = null
    private var dateString = ""
    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        // initialize fireBaseApp
        FirebaseApp.initializeApp(this)
        // log into fireBase
        val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
        // firebase login ui - opens if needed.
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(),
            RC_SIGN_IN
        )

        viewModel.inProgress?.observe(this, Observer {
            it?.let { inProgress ->
                progressBar.visibility = if (inProgress) View.VISIBLE else View.GONE
            }
        })

        // create the datePicker
        val mDAtePicker = DatePickerHelper.newInstance(this, object : DatePickerHelper.OnDateStringSetListener {
            override fun onDateSet(selectedDate: Calendar) {
                calendar = selectedDate
                dateString = DatePickerHelper.getWeekDayAndDate(selectedDate)
                btDate.text = dateString
            }
        })

        // show the date dialog.
        btDate.setOnClickListener { mDAtePicker.show() }

        // prepare the spinners.
        spCategory.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayOf("cat1", "cat2", "cat3"))

        spPaymentType.adapter =
            ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                arrayOf("BAR", "CARD", "PP", "N26", "GOOG")
            )

        btSubmit.setOnClickListener { _ ->
            viewModel.showProgress()

            val db = FirebaseDatabase.getInstance()

            user?.let {

                val paymentEntry = PaymentEntry(
                    weekDay = PaymentEntry.WeekDays.FRI,
                    date = dateString,
                    description = etDescription.text.toString(),
                    category = spCategory.selectedItem.toString(),
                    value = etValue.text.toString(),
                    paymentType = spPaymentType.selectedItem.toString()
                )

                // create the user specific table.
                val userRef = db.getReference("users/" + it.uid)
                val paymentRef = db.getReference("payments/")
                paymentRef.push()
                mRef.child("payments").child(it.uid).setValue(paymentEntry) { error, _ ->
                    if (error != null) Log.e(TAG, error.message)
                    else {
                        Toast.makeText(this, R.string.main_activity_entry_added, Toast.LENGTH_SHORT).show()
                        Log.i(TAG, "payment entry added!")
                        viewModel.hideProgress()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // check if firebase login was successful.
        if (requestCode == RC_SIGN_IN) {

            val response = IdpResponse.fromResultIntent(data)
            Log.d(TAG, "login activity response:" + response.toString())

            if (resultCode == Activity.RESULT_OK) {

                btSubmit.isEnabled = true
                user = FirebaseAuth.getInstance().currentUser
                Log.d(TAG, "Login successful!")
            } else {

                Toast.makeText(this, R.string.main_activity_login_failed, Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Login messed up!")
            }
        }
    }

    companion object {
        const val RC_SIGN_IN = 1
        private const val TAG = "MainActivity"
    }
}
