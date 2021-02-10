package com.example.uosfutsalcoachapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_view_tactic.*

class ViewTacticActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private val TAG = "ViewTacticActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_tactic)
        setTitle("Tactics")

        // Initialize Firebase Auth and firestore
        auth = Firebase.auth
        fStore = Firebase.firestore

       generateTacticList()



    }

    /*
    * this method gets all of the tactics from the firestore and displays them in the recycler view list
     */
    private fun generateTacticList() {
        val tacticList = ArrayList<TacticItem>()
        fStore.collection("tactics").get().addOnSuccessListener { result ->
            for (document in result) {
                val drawable = R.drawable.ic_tactic_item
                val item = TacticItem(drawable, "${document.id}")
                tacticList += item
            }
            recycler_view.adapter = MyAdapter(tacticList)
            recycler_view.layoutManager = LinearLayoutManager(this) //LinearLayoutManager creates vertical scrolling list
            recycler_view.setHasFixedSize(true) //optimization
        }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }
    }
}