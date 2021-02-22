package com.example.uosfutsalcoachapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MemberHomeScreenActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore : FirebaseFirestore
    private val TAG = "HomeScreenActivity"
    var userID = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_home_screen)
        val welcomeMsg : TextView = findViewById(R.id.tv_welcome)

        System.err.println(callingActivity?.className)
        // Initialize Firebase Auth and firestore
        auth = Firebase.auth
        fStore = Firebase.firestore
        val user = Firebase.auth.currentUser
        if (user != null) {
            userID = user.uid
        }
        // val documentReference : DocumentReference = fStore.collection("users").document(userID)
        fStore.collection("users").get().addOnSuccessListener { result ->
            for (document in result) {
                Log.d(TAG, "${userID} => ${document.data}")
                if(userID == document.id){
                    welcomeMsg.setText("Welcome ${document.data.get("Full Name").toString()}")
                }
            }
        }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
        val btnLogout : Button = findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener{
            //log the current user out and redirect him to the login activity
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(baseContext, "Logged out successfuly.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()

        }

        val btnChat : Button = findViewById(R.id.btnMemberChat)
        btnChat.setOnClickListener{
            startActivity(Intent(this,ChatActivity::class.java))
        }
    }
}