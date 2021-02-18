package com.example.uosfutsalcoachapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import org.w3c.dom.Text
import java.lang.Exception

class ProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private var memberName = ""
    private val TAG = "ProfileActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
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

        val memberFullName : TextView = findViewById(R.id.tv_fullName)
        val memberEmailID : TextView = findViewById(R.id.tv_emailID)
        val studentID : TextView = findViewById(R.id.tv_studentID)

        getMemberDetails(memberFullName,memberEmailID,studentID)
        //val memberFullName : TextView = findViewById(R.id.tv_fullName)
    }

    /*
* this method navigates the captain/member to the memberActivity in order to show the details/profile of a team member
*/
    private fun getMemberDetails(memberFullName:TextView, memberEmail : TextView, studentID : TextView) {
        try {
            fStore.collection("users").get().addOnSuccessListener { result ->
                for (document in result) {
                    if (document.data.get("Full Name").toString() == memberName) {
                        Picasso.get().load(document.data.get("User Photo").toString()).into(memberPicture)
                        memberFullName.text = "Full Name: ${document.data.get("Full Name") as CharSequence?}"
                        memberEmail.text = "Email ID: ${document.data.get("Email ID") as CharSequence?}"
                        studentID.text = "Student ID: ${document.data.get("studentID") as CharSequence?}"
                        fStore.collection("users").document(memberName).get()
                            .addOnSuccessListener {
                                Log.d(
                                    TAG,
                                    "DocumentSnapshot data: ${document.data}"
                                )
                            }
                            .addOnFailureListener { e -> Log.w(TAG, "No such document", e) }
                    }
                }
            }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }
        }catch(e : Exception){
            Toast.makeText(this,"No member is currently selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}