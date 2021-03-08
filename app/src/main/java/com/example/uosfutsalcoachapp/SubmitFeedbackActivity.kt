package com.example.uosfutsalcoachapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_captain_view_tactic.*
import kotlinx.android.synthetic.main.activity_chat.*

class SubmitFeedbackActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private val TAG = "SubmitFeedbackActivity"
    private val feedbackList = ArrayList<FeedbackItem>()
    private val adapter = FeedbackAdapter(feedbackList)
    private var tacticName = ""
    private var userName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_feedback)

        // Initialize Firebase Auth and firestore
        auth = Firebase.auth
        fStore = Firebase.firestore
        //call the action bar
        val actionBar = supportActionBar
        tacticName = intent.getStringExtra("tacticName")!!
        //show the back button in action bar
        if (actionBar != null) {
            //set action bar title
            actionBar.title = "$tacticName - Feedback"
            //set back button
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        generateFeedbacks()
    }

    /*
    * this method gets all feedbacks for the tactic from the firestore and displays them in the recycler view list
    */
    private fun generateFeedbacks() {
        fStore.collection("feedbacks").get().addOnSuccessListener { result ->
            for (document in result) {
                if(document.data.get("Tactic") == tacticName) {
                    val drawable = R.drawable.ic_feedback_icon
                    val item = FeedbackItem(drawable, document.data.get("AuthorId").toString() ,document.data.get("Content").toString(),document.data.get("time").toString())
                    feedbackList += item
                }
            }
            feedbackList.sortByDescending{it.time}
            recycler_view.adapter = adapter
            recycler_view.layoutManager =
                LinearLayoutManager(this) //LinearLayoutManager creates vertical scrolling list
            recycler_view.setHasFixedSize(true) //optimization
            if(recycler_view.size == 0){
                Toast.makeText(this, "THERE ARE NO REVIEWS FOR THIS TACTIC", Toast.LENGTH_SHORT).show()
            }
        }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}