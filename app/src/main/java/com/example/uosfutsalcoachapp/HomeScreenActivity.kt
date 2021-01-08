package com.example.uosfutsalcoachapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class HomeScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        System.err.println(callingActivity?.className)
        val btnLogout : Button = findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(baseContext, "Logged out successfuly.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()

        }
    }
}