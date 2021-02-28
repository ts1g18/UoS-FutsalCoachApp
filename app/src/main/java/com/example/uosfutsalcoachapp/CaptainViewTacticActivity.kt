package com.example.uosfutsalcoachapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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

class CaptainViewTacticActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private val TAG = "CaptainViewTacticActivity"
    private val tacticList = ArrayList<TacticItem>()
    private val adapter = TacticAdapter(tacticList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_captain_view_tactic)
        setTitle("Tactics")
        //call the action bar
        val actionBar = supportActionBar
        //show the back button in action bar
        if(actionBar != null){
            //set action bar title
            actionBar!!.title = "Tactics"
            //set back button
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        // Initialize Firebase Auth and firestore
        auth = Firebase.auth
        fStore = Firebase.firestore

        generateTacticList()

        val deleteTacticBtn : Button = findViewById(R.id.btnDeleteTactic)
        deleteTacticBtn.setOnClickListener{
            deleteTactic()
        }

        val viewTacticBtn : Button = findViewById(R.id.btnViewTactic)
        viewTacticBtn.setOnClickListener{
            viewTactic()
        }

        val editTacticBtn : Button = findViewById(R.id.btnEditTactic)
        editTacticBtn.setOnClickListener{
            goToEditTacticScreen()
        }

    }


    /*
    * this method gets all of the tactics from the firestore and displays them in the recycler view list
     */
    private fun generateTacticList() {
        fStore.collection("tactics").get().addOnSuccessListener { result ->
            for (document in result) {
                val drawable = R.drawable.ic_tactic_item
                val item = TacticItem(drawable, "${document.id}")
                tacticList += item
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
    * this method deletes the currently selected tactic
     */
    private fun deleteTactic() {
        try {
            //get the name of the tactic to be removed so that we can compare it wwith document.id from firestore and remove from dataabase as well
            var tacticToBeRemoved = tacticList.get(adapter.getSelectedPosition()!!).text1
            //remove it from list
            tacticList.removeAt(adapter.getSelectedPosition()!!)
            adapter.notifyItemRemoved(adapter.getSelectedPosition()!!)
            fStore.collection("tactics").get().addOnSuccessListener { result ->
                for (document in result) {
                    if (document.id == tacticToBeRemoved) {
                        fStore.collection("tactics").document(tacticToBeRemoved).delete()
                            .addOnSuccessListener {
                                Log.d(
                                    TAG,
                                    "DocumentSnapshot successfully deleted!"
                                )
                            }
                            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
                    }
                }
            }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }
        }catch(e : Exception){
            Toast.makeText(this,"No tactic is currently selected",Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToEditTacticScreen(){
        try {
            //get the name of the tactic to be removed so that we can compare it wwith document.id from firestore and remove from dataabase as well
            var tacticToView = tacticList.get(adapter.getSelectedPosition()!!).text1

            fStore.collection("tactics").get().addOnSuccessListener { result ->
                for (document in result) {
                    if (document.id == tacticToView) {
                        fStore.collection("tactics").document(tacticToView).get()
                            .addOnSuccessListener {
                                Log.d(
                                    TAG,
                                    "DocumentSnapshot data: ${document.data}"
                                )
                                val intent = Intent(this@CaptainViewTacticActivity, EditTacticActivity::class.java)
                                intent.putExtra("tacticName", tacticToView)
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
            Toast.makeText(this,"No tactic is currently selected",Toast.LENGTH_SHORT).show()
        }
    }



    /*
    * this method navigates the captain/member to the TacticActivity in order to demonstrate the movements of the currently selected tactic
     */
    private fun viewTactic() {
        try {
            //get the name of the tactic to be removed so that we can compare it wwith document.id from firestore and remove from dataabase as well
            var tacticToView = tacticList.get(adapter.getSelectedPosition()!!).text1

            fStore.collection("tactics").get().addOnSuccessListener { result ->
                for (document in result) {
                    if (document.id == tacticToView) {
                        fStore.collection("tactics").document(tacticToView).get()
                            .addOnSuccessListener {
                                Log.d(
                                    TAG,
                                    "DocumentSnapshot data: ${document.data}"
                                )
                                val intent = Intent(this@CaptainViewTacticActivity, TacticActivity::class.java)
                                intent.putExtra("tacticName", tacticToView)
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
            Toast.makeText(this,"No tactic is currently selected",Toast.LENGTH_SHORT).show()
        }
    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}