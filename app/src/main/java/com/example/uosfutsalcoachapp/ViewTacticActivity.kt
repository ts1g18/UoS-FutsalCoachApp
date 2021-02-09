package com.example.uosfutsalcoachapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ViewTacticActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore : FirebaseFirestore
    private val TAG = "ViewTacticActivity"
    var tacticNames = ArrayList<String>()
    var tacticData = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_tactic)
        setTitle("Tactics")

        // Initialize Firebase Auth and firestore
        auth = Firebase.auth
        fStore = Firebase.firestore
        getAndDisplayTactics()

    }
    /*
    * method that gets the tactics from the fstore database and displays them in the listview
     */
    fun getAndDisplayTactics(){
        fStore.collection("tactics").get().addOnSuccessListener { result ->
            for (document in result) {
                tacticNames.add(document.id)
                val tacticList : ListView = findViewById(R.id.tactic_list_view)
                val adapter = ArrayAdapter(this,R.layout.tactic_list_layout, tacticNames)
                tacticList.setAdapter(adapter)
            }
        }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }

    }

}