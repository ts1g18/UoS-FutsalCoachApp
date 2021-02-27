package com.example.uosfutsalcoachapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.okhttp.internal.Internal.instance
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_signup.*
import okhttp3.internal.Internal.instance
import java.lang.Exception
import java.util.*

class EditProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private var memberName = ""
    private var studentId = ""
    private var emailId = ""
    private var role = ""
    private val TAG = "EditProfileActivity"
    var userID = ""
    private var selectedPhotoUri: Uri? = null
    private var imageUrl: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        // Initialize Firebase Auth and firestore
        auth = Firebase.auth
        fStore = Firebase.firestore
        //call the action bar
        val actionBar = supportActionBar
        memberName = intent.getStringExtra("memberName")!!
        studentId = intent.getStringExtra("studentId")!!
        emailId = intent.getStringExtra("emailId")!!
        imageUrl = intent.getStringExtra("imageUrl")
        role = intent.getStringExtra("role")!!
        //show the back button in action bar
        if (actionBar != null) {
            //set action bar title
            actionBar.title = memberName
            //set back button
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        getPhoto()

        val userPhoto = memberPictureEdit
        userPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
            //we need to show photo in button
        }



        val btnSaveDetails: Button = findViewById(R.id.btnSaveEditDetails)
        btnSaveDetails.setOnClickListener {
            uploadImageAndSave()
        }

    }

    private fun saveDetails() {
        val user = Firebase.auth.currentUser
        userID = user?.uid.toString()

        // Create a new user with a fullname and username(emailID) and student id
        val updatedUser = hashMapOf(
            "Full Name" to tv_fullNameEdit.text.toString(),
            "Email ID" to tv_emailIDEdit.text.toString(),
            "Student ID" to tv_studentIDEdit.text.toString(),
            "User Photo" to imageUrl.toString(),
            "Role" to tv_roleEdit.text.toString()
        )
        fStore.collection("users").document(userID).set(updatedUser, SetOptions.merge())
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: $userID")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
        val intent = Intent(this@EditProfileActivity, ProfileActivity::class.java)
        memberName = tv_fullNameEdit.text.toString()
        intent.putExtra("memberName", memberName)
        startActivity(intent)
        finish()
    }

    //this method is called everytime the photo selector is called
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            //proceed and check what the selected image was
            selectedPhotoUri = data.data //represents the location where that image is stored on the device
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            memberPictureOutlineEdit.setImageBitmap(bitmap)
            selectphoto_imageview_edit.alpha = 0f
//            val bitmapDrawable = BitmapDrawable(bitmap)
//            btn_select_photo.setBackgroundDrawable(bitmapDrawable)
        }
    }
    /*
* this method navigates the captain/member to the memberActivity in order to show the details/profile of a team member
*/
    private fun getPhoto() {
        try {
            fStore.collection("users").get().addOnSuccessListener { result ->
                for (document in result) {
                    if (document.data.get("Full Name").toString() == memberName) {
                        Picasso.get().load(document.data.get("User Photo").toString())
                            .into(memberPictureOutlineEdit)
                        tv_roleEdit.text = document.data.get("Role").toString()
                        tv_emailIDEdit.text = document.data.get("Email ID").toString()
                        tv_fullNameEdit.setText(document.data.get("Full Name").toString())
                        tv_studentIDEdit.setText(document.data.get("Student ID").toString())
                    }
                }
            }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }
        } catch (e: Exception) {
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id = item.itemId
        if (id == R.id.save_details) {
            saveDetails()
            return true
        } else if (id == R.id.log_out_profile) {
            logout()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //innflate the menu; this adds items to action bar if present
        menuInflater.inflate(R.menu.menu_main_profile_edit, menu)
        return true
    }

    //log the current user out and redirect him to the login activity
    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(baseContext, "Logged out successfuly.", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    //upload user photo to firebase storage
    private fun uploadImageAndSave() {
        if (selectedPhotoUri == null) {
            saveDetails()
            return
        }
        //random long string
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        //putFile takes uri
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")

                //need to have access to file location
                ref.downloadUrl.addOnSuccessListener {
                    Log.d(TAG, "File location: ${it}")
                    imageUrl = it.toString()
                    saveDetails()
                }
            }
    }
}