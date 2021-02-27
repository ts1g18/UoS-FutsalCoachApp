package com.example.uosfutsalcoachapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
    var userName = ""
    var role = ""
    var studentId = ""
    var emailId = ""
    var userPhoto = ""

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
                    userName = document.data.get("Full Name").toString()
                }
            }
        }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        val btnViewTactics : Button = findViewById(R.id.btnMemberViewTactics)
        btnViewTactics.setOnClickListener{
            startActivity(Intent(this,MemberViewTacticActivity::class.java))
        }

        val btnViewMembers : Button = findViewById(R.id.btnMemberViewMembers)
        btnViewMembers.setOnClickListener{
            startActivity(Intent(this,MemberViewMembersActivity::class.java))
        }

        val btnChat : Button = findViewById(R.id.btnMemberChat)
        btnChat.setOnClickListener{
            startActivity(Intent(this,ChatActivity::class.java))
        }



        val btnLogout : Button = findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener{
          logout()
        }

    }

    //log the current user out and redirect him to the login activity
    private fun logout(){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(baseContext, "Logged out successfuly.", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id = item.itemId
        if (id == R.id.view_my_profile){
            val intent = Intent(this@MemberHomeScreenActivity, ProfileActivity::class.java)
            intent.putExtra("memberName", userName)
            startActivity(intent)
            return true
        } else if(id == R.id.log_out){
            logout()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //innflate the menu; this adds items to action bar if present
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


}