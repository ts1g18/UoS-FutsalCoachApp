package com.example.uosfutsalcoachapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val actionBar = supportActionBar
        //show the back button in action bar
        if (actionBar != null) {
            //set action bar title
            actionBar.title = "USFC Team Chat"
            //set back button
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        btnSendMessage.setOnClickListener{
            val message = et_type_message.text.toString()
            //if user has not typed something but pressed send
            if(message == ""){
                Toast.makeText(this, "Please write message first",Toast.LENGTH_SHORT).show()

            }else{
                sendMessage()

            }
        }
    }

    private fun sendMessage() {

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}