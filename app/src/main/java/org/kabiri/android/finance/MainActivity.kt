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

        db.collection("users").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener {exception ->
                Log.w(TAG, "error getting documents.", exception)
            }


    }
}
