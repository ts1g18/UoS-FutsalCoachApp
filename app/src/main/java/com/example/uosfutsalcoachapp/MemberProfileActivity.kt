package com.example.uosfutsalcoachapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MemberProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private var memberName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_profile)
        // Initialize Firebase Auth and firestore
        auth = Firebase.auth
        fStore = Firebase.firestore
        //call the action bar
        val actionBar = supportActionBar
        memberName = intent.getStringExtra("memberName")!!
        //show the back button in action bar
        if (actionBar != null) {
            //set action bar title
            actionBar.title = memberName
            //set back button
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
    }
}