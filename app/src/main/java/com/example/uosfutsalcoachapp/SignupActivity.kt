package com.example.uosfutsalcoachapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class SignupActivity : AppCompatActivity() {

    //declare instance of FirebaseAuth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        // Initialize Firebase Auth
        auth = Firebase.auth
        val btnSignUp: Button = findViewById(R.id.btnSignUp)
        btnSignUp.setOnClickListener {
                signUpUser()
        }
    }

        //this function will sign up a new user if the details provided are valid
        //if any error occurs we need to stop the execution there so we need to call return at the end of each check
        private fun signUpUser() {
            val username : TextView = findViewById(R.id.et_username);
            val password : TextView = findViewById(R.id.et_password);


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

            auth.createUserWithEmailAndPassword(username.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
//                        // Sign in success, update UI with the signed-in user's information
//                        Log.d(TAG, "createUserWithEmail:success")
//                        val user = auth.currentUser
//                        updateUI(user)
                        Toast.makeText(baseContext, "Signed up successfuly.",
                            Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,LoginActivity::class.java))
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Sign up failed. Try again later.",
                            Toast.LENGTH_SHORT).show()
//                        updateUI(null)
                    }

                    // ...
                }
        }

    private fun updateUI(user: FirebaseUser?) {

    }


}