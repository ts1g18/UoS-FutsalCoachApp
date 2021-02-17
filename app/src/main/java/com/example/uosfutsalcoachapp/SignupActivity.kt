package com.example.uosfutsalcoachapp

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_signup.*
import java.net.URL
import java.util.*


class SignupActivity : AppCompatActivity() {

    //declare instance of FirebaseAuth
    private lateinit var auth: FirebaseAuth
    val fStore = Firebase.firestore
    var userID = ""
    private val TAG = "SignupActivity"
    private var selectedPhotoUri: Uri? = null
    private var imageUrl: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        // Initialize Firebase Auth
        auth = Firebase.auth
        val btnSignUp: Button = findViewById(R.id.btnSignUp)
        btnSignUp.setOnClickListener {
            uploadImageToFirebaseStorage()
        }

        val userPhoto: Button = findViewById(R.id.btn_select_photo)
        userPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
            //we need to show photo in button

        }
    }



    //this method is called everytime the photo selector is called
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            //proceed and check what the selected image was
            selectedPhotoUri = data.data //represents the location where that image is stored on the device
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            selectphoto_imageview_register.setImageBitmap(bitmap)
            btn_select_photo.alpha = 0f
//            val bitmapDrawable = BitmapDrawable(bitmap)
//            btn_select_photo.setBackgroundDrawable(bitmapDrawable)
        }
    }

    //this function will sign up a new user if the details provided are valid
    //if any error occurs we need to stop the execution there so we need to call return at the end of each check
    private fun signUpUser() {
        val username: TextView = findViewById(R.id.et_username);
        val password: TextView = findViewById(R.id.et_password);
        val fullName: TextView = findViewById(R.id.et_fullname);
        val studentID: TextView = findViewById(R.id.et_studentID);


        //check if an email address is entered
        if (username.text.toString().isEmpty()) {
            username.error = "Please enter email"
            username.requestFocus()
            return
        }

        //check if a VALID email address is entered
        if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()) {
            username.error = "Please enter a valid email"
            username.requestFocus()
            return
        }

        //check if a password is entered
        if (password.text.toString().isEmpty()) {
            password.error = "Please enter password"
            password.requestFocus()
            return
        }


        //if checks are passed then create the user
        auth.createUserWithEmailAndPassword(username.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
//                        // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "createUserWithEmail:success")
//                        val user = auth.currentUser
//                        updateUI(user)

                    val user = Firebase.auth.currentUser

                    //send a verification email to the registered email ID
                    user!!.sendEmailVerification()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
//                                uploadImageToFirebaseStorage()
                                Toast.makeText(baseContext,"Signed up successfuly. A verification email has been sent to your email address",Toast.LENGTH_SHORT).show()
                                userID = user.uid
                                // Create a new user with a fullname and username(emailID) and student id
                                val user = hashMapOf(
                                    "Full Name" to fullName.text.toString(),
                                    "Email ID" to username.text.toString(),
                                    "Student ID" to studentID.text.toString(),
                                    "User Photo" to imageUrl
                                )
                                val documentReference: DocumentReference =
                                    fStore.collection("users").document(userID)
                                // Add a new document with a generated ID
                                documentReference.set(user)
                                    .addOnSuccessListener { documentReference ->
                                        Log.d(TAG, "DocumentSnapshot added with ID: $userID")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w(TAG, "Error adding document", e)
                                    }
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                        }

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Sign up failed. Try again later.",
                        Toast.LENGTH_SHORT
                    ).show()
//                        updateUI(null)
                }

                // ...
            }
    }

    //upload user photo to firebase
    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return
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
                    signUpUser()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {

    }

}