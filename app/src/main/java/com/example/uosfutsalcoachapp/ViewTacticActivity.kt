package com.example.uosfutsalcoachapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_view_tactic.*
import java.lang.Exception

class ViewTacticActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private val TAG = "ViewTacticActivity"
    private val tacticList = ArrayList<TacticItem>()
    private val adapter = MyAdapter(tacticList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_tactic)
        setTitle("Tactics")

        // Initialize Firebase Auth and firestore
        auth = Firebase.auth
        fStore = Firebase.firestore

        generateTacticList()

        val deleteTacticBtn : Button = findViewById(R.id.btnDeleteTactic)
        deleteTacticBtn.setOnClickListener{
            deleteTactic()
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

}