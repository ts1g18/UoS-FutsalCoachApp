package com.example.uosfutsalcoachapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_captain_view_tactic.*
import java.lang.Exception

class MemberViewMembersActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private val TAG = "ViewTacticActivity"
    private val memberList = ArrayList<MemberItem>()
    private val adapter = MemberAdapter(memberList)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_view_members_activity)
        //call the action bar
        val actionBar = supportActionBar
        //show the back button in action bar
        if (actionBar != null) {
            //set action bar title
            actionBar!!.title = "Team Members"
            //set back button
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        // Initialize Firebase Auth and firestore
        auth = Firebase.auth
        fStore = Firebase.firestore
        generateMemberList()

        val btnViewMember : Button = findViewById(R.id.btnMemberViewMember)
        btnViewMember.setOnClickListener{
            viewMember()
        }
    }

    /*
   * this method gets all of the team members from the firestore and displays them in the recycler view list
   */
    private fun generateMemberList() {
        fStore.collection("users").get().addOnSuccessListener { result ->
            for (document in result) {
                val drawable = R.drawable.ic_member_icon
                val item = MemberItem(drawable, "${document.data.get("Full Name").toString()}")
                memberList += item
            }
            recycler_view.adapter = adapter
            recycler_view.layoutManager =
                LinearLayoutManager(this) //LinearLayoutManager creates vertical scrolling list
            recycler_view.setHasFixedSize(true) //optimization
        }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    /*
  * this method navigates the captain/member to the memberActivity in order to show the details/profile of a team member
   */
    private fun viewMember() {
        try {
            //get the name of the member to be removed so that we can compare it wwith document.id from firestore and remove from database as well
            var memberToView = memberList.get(adapter.getSelectedPosition()!!).text1
            fStore.collection("users").get().addOnSuccessListener { result ->
                for (document in result) {
                    if (document.data.get("Full Name").toString() == memberToView) {
                        fStore.collection("users").document(memberToView).get()
                            .addOnSuccessListener {
                                Log.d(
                                    TAG,
                                    "DocumentSnapshot data: ${document.data}"
                                )
                                val intent = Intent(this@MemberViewMembersActivity, ProfileActivity::class.java)
                                intent.putExtra("memberName", memberToView)
                                startActivity(intent)
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