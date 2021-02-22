package com.example.uosfutsalcoachapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*
import kotlin.collections.ArrayList


class ChatActivity : AppCompatActivity() {
    private lateinit var fStore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private val TAG = "ChatActivity"

    private val chatMessageList = ArrayList<ChatMessage>()
    private val adapter = MessageListAdapter(chatMessageList)

    override fun onCreate(savedInstanceState: Bundle?) {
        fStore = Firebase.firestore
        auth = Firebase.auth

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val actionBar = supportActionBar!!
        //show the back button in action bar
        //set action bar title
        actionBar.title = "USFC Team Chat"
        //set back button
        actionBar.setDisplayHomeAsUpEnabled(true)

        getUsernameAndPhoto(auth.currentUser!!.uid)

        generateMessageList()

        btnSendMessage.setOnClickListener {
            val message = et_type_message.text.toString()
            //if user has not typed something but pressed send
            if (message == "") {
                Toast.makeText(this, "Please write message first", Toast.LENGTH_SHORT).show()
            } else {
                sendMessage(message)
                et_type_message.text.clear()
            }
        }
    }

    private fun generateMessageList() {
        fStore.collection("chat").get().addOnSuccessListener { result ->
            for (document in result) {
                val item = ChatMessage(
                    document.data.get("content") as String,
                    document.data.get("sender") as String,
                    document.data.get("time").toString()
                )
                chatMessageList += item

            }
            recycler_view_chats.adapter = adapter
            recycler_view_chats.layoutManager =
                LinearLayoutManager(this) //LinearLayoutManager creates vertical scrolling list
            recycler_view_chats.setHasFixedSize(true) //optimization
        }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting chats from firestore.", exception)
            }
    }

    private fun sendMessage(message: String) {
        // The name of the document corresponding to the currently logged in user.
        val currentUserName = auth.currentUser!!.uid
        /*
        access the collection named "chat"
        this collection is a list of maps, with each map representing one chat message.
        each map contains 3 fields:
        content: The chat content (string)
        sender:     The name of the document  in the 'users' collection corresponding to the user
                    who sent the message
        time: The timestamp of when the database receives the message.
        */
        fStore.collection("chat").document()
            .set(
                hashMapOf(
                    "content" to message,
                    "sender" to currentUserName,
                    "time" to Date(Timestamp.now().seconds*1000).toLocaleString()
                )
            )
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    private fun getUsernameAndPhoto(userId : String){
        fStore.collection("users").get().addOnSuccessListener{ result ->
            for (document in result){
                if(document.id == userId){
                    Picasso.get().load(document.data.get("User Photo").toString()).into(profile_image_chat)
                    tv_username_chat.setText(document.get("Full Name").toString())
                }
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}