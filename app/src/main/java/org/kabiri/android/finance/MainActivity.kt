package org.kabiri.android.finance

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // initialize fireBaseApp
        FirebaseApp.initializeApp(this)
        // log into fireBase
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        // firebase login ui - opens if needed.
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)

        // prepare the spinners.
        spCategory.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayOf("cat1", "cat2", "cat3"))
        spPaymentType.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayOf("BAR", "CARD", "PP", "N26", "GOOG"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // successfully signed into firebase.
                btSubmit.isEnabled = true
                user = FirebaseAuth.getInstance().currentUser
            } else {
                // login messed up!
            }
        }

        btSubmit.setOnClickListener {
            progressBar.visibility = View.VISIBLE

            val db = FirebaseDatabase.getInstance()

            user?.let {
                // create the user specific table.
                val mRef = db.getReference(it.uid)
                mRef.setValue("Some new user data") { databaseError, databaseReference ->
                    Toast.makeText(this@MainActivity, "something happened!", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.INVISIBLE
                }
            }
        }
    }

    companion object {
        const val RC_SIGN_IN = 1
    }
}
