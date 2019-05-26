package org.kabiri.android.finance

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initialize fireBaseApp
        FirebaseApp.initializeApp(this)

        // access the cloud fireStore db
        val db = FirebaseFirestore.getInstance()

        // add a new user
//        val user = HashMap<String, Any>()
//        user["first"] = "Alan"
//        user["middle"] = "Mathison"
//        user["last"] = "Turring"
//        user["born"] = 1912
//
//        // Add a new document with a generated ID
//        db.collection("users")
//            .add(user)
//            .addOnSuccessListener { docReference ->
//                Log.d(TAG, "DocumentSnapshot added with ID: ${docReference.id}")
//            }
//            .addOnFailureListener { e ->
//                Log.w(TAG, "Error adding document", e)
//            }

//        db.collection("users").get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    Log.d(TAG, "${document.id} => ${document.data}")
//                }
//            }
//            .addOnFailureListener {exception ->
//                Log.w(TAG, "error getting documents.", exception)
//            }

        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // successfully signed in.
                val user = FirebaseAuth.getInstance().currentUser
            } else {
                // login messed up!
            }
        }
    }

    companion object {
        const val RC_SIGN_IN = 1
    }
}
