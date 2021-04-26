package com.example.uosfutsalcoachapp


import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {
    //declare instance of FirebaseAuth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val tv_signup: TextView = findViewById(R.id.tv_signup)
        tv_signup.setOnClickListener{
            startActivity(Intent(this, SignupActivity::class.java))
        }
        val tv_forgotPass: TextView = findViewById(R.id.tv_forgotPass)
        tv_forgotPass.setOnClickListener{

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Please enter your registered email address.")

            val view = layoutInflater.inflate(R.layout.dialog_forgot_password,null)
            val username = view.findViewById<EditText>(R.id.et_username)

            builder.setView((view))
            builder.setPositiveButton("Reset", DialogInterface.OnClickListener{_,_ ->
                forgotPassword(username)
            })
            builder.setNegativeButton("Close", DialogInterface.OnClickListener{_,_ -> })
            builder.show()
        }

        val btnLogin : Button = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener{
            userLogin()
        }


    }

    private fun forgotPassword(username: EditText){
        if(username.text.toString().isEmpty()){
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()){
            return
        }

        // if valid then proceed with sending a reset password email
        Firebase.auth.sendPasswordResetEmail(username.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this,"Reset password email sent to your registered emailID.", Toast.LENGTH_SHORT).show()
                    }
                }
    }
    /*
    this function is called when the user presses the Login button
     ensures that username and password are entered in a valid format
     allows users to login if they are registered and verified
     */
    private fun userLogin() {
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

        //if checks pass then we need to sign in the user
        auth.signInWithEmailAndPassword(username.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Login failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }


    }

    //when initialising the activity I have to check whether the user is already signed in
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        //check if the current user is not null
        //if not null navigate user to homescreen
        //else failed
        if (currentUser != null){
            //only allow user to proceed to homepage if the email ID has been verified
            if(currentUser.isEmailVerified) {
                if(currentUser.email == "ts1g18@soton.ac.uk") {
                    startActivity(Intent(this, HomeScreenActivity::class.java))
                    finish()
                }else{
                    startActivity(Intent(this, MemberHomeScreenActivity::class.java))
                    finish()
                }
            }else{
                Toast.makeText(baseContext, "Please verify your email address!",Toast.LENGTH_SHORT).show()
            }
        }
    }

}
