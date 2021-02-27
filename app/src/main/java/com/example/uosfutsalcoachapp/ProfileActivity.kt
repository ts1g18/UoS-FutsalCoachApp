package com.example.uosfutsalcoachapp

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private var memberName = ""
    private var role = ""
    private var userPhoto = ""
    private var emailId = ""
    private var studentId = ""
    private val TAG = "ProfileActivity"
    private var imageUrl = ""
    private var isMe = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        // Initialize Firebase Auth and firestore
        auth = Firebase.auth
        fStore = Firebase.firestore
        //call the action bar
        val actionBar = supportActionBar
        memberName = intent.getStringExtra("memberName")!!
        try {
            isMe = intent.getStringExtra("isMe")!!
        }catch(e:Exception){

        }

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
        val role : TextView = findViewById(R.id.tv_role)

        getMemberDetails(memberFullName, memberEmailID, studentID, role)
    }

    /*
* this method navigates the captain/member to the memberActivity in order to show the details/profile of a team member
*/
    private fun getMemberDetails(
        memberFullName: TextView,
        memberEmail: TextView,
        studentID: TextView,
        role: TextView
    ) {
        try {
            fStore.collection("users").get().addOnSuccessListener { result ->
                for (document in result) {
                    if (document.data.get("Full Name").toString() == memberName) {
                        imageUrl = document.data.get("User Photo").toString()
                        Picasso.get().load(document.data.get("User Photo").toString()).into(
                            memberPictureOutline
                        )
                        memberFullName.text = "Full Name: ${document.data.get("Full Name") as CharSequence?}"
                        memberEmail.text = "Email ID: ${document.data.get("Email ID") as CharSequence?}"
                        studentID.text = "Student ID: ${document.data.get("Student ID") as CharSequence?}"
                        role.text = "Role: ${document.data.get("Role") as CharSequence?}"
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
        }catch (e: Exception){
            Toast.makeText(this, "No member is currently selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id = item.itemId
        if (id == R.id.edit_profile){
            val intent = Intent(this@ProfileActivity, EditProfileActivity::class.java)
            intent.putExtra("memberName", memberName)
            intent.putExtra("studentId", tv_studentID.text.toString().replace("Student ID: ", ""))
            intent.putExtra("emailId", tv_emailID.text)
            intent.putExtra("role", tv_role.text)
            intent.putExtra("imageUrl", imageUrl)
            startActivity(intent)
            finish()
            return true
        } else if(id == R.id.log_out_profile){
            logout()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(isMe == "true") {
            //innflate the menu; this adds items to action bar if present
            menuInflater.inflate(R.menu.menu_main_profile, menu)
        }else if(isMe == ""){
            menuInflater.inflate(R.menu.menu_main_profile, menu)
            return true
        }else if(isMe == "false") {
            return true
        }
        return true
    }
    //log the current user out and redirect him to the login activity
    private fun logout(){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(baseContext, "Logged out successfuly.", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
