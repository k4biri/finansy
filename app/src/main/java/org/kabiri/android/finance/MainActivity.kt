package org.kabiri.android.finance

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.FirebaseApp
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

        // create a new user
        val user = HashMap<String, Any>()
        user["first"] = "Ada"
        user["last"] = "Lovelace"
        user["born"] = 1815

        // add a new doc with a generated id
        db.collection("users")
            .add(user)
            .addOnSuccessListener { docReference ->
                Log.d(TAG, "Document snapshot added with id: ${docReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
}
