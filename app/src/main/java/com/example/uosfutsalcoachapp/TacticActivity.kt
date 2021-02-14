package com.example.uosfutsalcoachapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_tactic.*
import java.lang.Exception

class TacticActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private var tacticName = ""
    var player1Pos = ArrayList<Pair<Float, Float>>()
    var player2Pos = ArrayList<Pair<Float, Float>>()
    var player3Pos = ArrayList<Pair<Float, Float>>()
    var player4Pos = ArrayList<Pair<Float, Float>>()
    var player5Pos = ArrayList<Pair<Float, Float>>()
    var ballPos = ArrayList<Pair<Float,Float>>()
    var opponent1Pos = ArrayList<Pair<Float, Float>>()
    var opponent2Pos = ArrayList<Pair<Float, Float>>()
    var opponent3Pos = ArrayList<Pair<Float, Float>>()
    var opponent4Pos = ArrayList<Pair<Float, Float>>()
    var opponent5Pos = ArrayList<Pair<Float, Float>>()
    private val TAG = "TacticActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tactic)
        // Initialize Firebase Auth and firestore
        auth = Firebase.auth
        fStore = Firebase.firestore
        //call the action bar
        val actionBar = supportActionBar
        tacticName = intent.getStringExtra("tacticName")!!
        //show the back button in action bar
        if (actionBar != null) {
            //set action bar title
            actionBar.title = tacticName
            //set back button
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        val previewTacticBtn: Button = findViewById(R.id.btnPreviewTactic)
        previewTacticBtn.setOnClickListener {
            try{
                demonstrateTactic()
                clearPlayerPosArrays()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }

    /*
    * this method will get the position of each player in each frame from the firestore for the specific tactic
     */
    private fun demonstrateTactic() {
        fStore.collection("tactics").get().addOnSuccessListener { result ->
            for (document in result) {
                if (document.id == tacticName) {
                    fStore.collection("tactics").document(tacticName).get()
                        .addOnSuccessListener {
                            for (frame in document.data) {
                                if(frame.key == "player1"){
                                    var player1Map = frame.value as ArrayList<HashMap<String,Float>>
                                    for(position in player1Map){
                                        player1Pos.add(Pair(position.get("first"),position.get("second")) as Pair<Float, Float>)
                                    }
                                }
                                else if(frame.key == "player2"){
                                    var player2Map = frame.value as ArrayList<HashMap<String,Float>>
                                    for(position in player2Map){
                                        player2Pos.add(Pair(position.get("first"),position.get("second")) as Pair<Float, Float>)
                                    }
                                }
                                else if(frame.key == "player3"){
                                    var player3Map = frame.value as ArrayList<HashMap<String,Float>>
                                    for(position in player3Map){
                                        player3Pos.add(Pair(position.get("first"),position.get("second")) as Pair<Float, Float>)
                                    }
                                }
                                else if(frame.key == "player4"){
                                    var player4Map = frame.value as ArrayList<HashMap<String,Float>>
                                    for(position in player4Map){
                                        player4Pos.add(Pair(position.get("first"),position.get("second")) as Pair<Float, Float>)
                                    }
                                }
                                else if(frame.key == "player5"){
                                    var player5Map = frame.value as ArrayList<HashMap<String,Float>>
                                    for(position in player5Map){
                                        player5Pos.add(Pair(position.get("first"),position.get("second")) as Pair<Float, Float>)
                                    }
                                }
                                else if(frame.key == "ball"){
                                    var ballMap = frame.value as ArrayList<HashMap<String,Float>>
                                    for(position in ballMap){
                                        ballPos.add(Pair(position.get("first"),position.get("second")) as Pair<Float, Float>)
                                    }
                                }
                                else if(frame.key == "opponent1"){
                                    var opponent1Map = frame.value as ArrayList<HashMap<String,Float>>
                                    for(position in opponent1Map){
                                        opponent1Pos.add(Pair(position.get("first"),position.get("second")) as Pair<Float, Float>)
                                    }
                                }
                                else if(frame.key == "opponent2"){
                                    var opponent2Map = frame.value as ArrayList<HashMap<String,Float>>
                                    for(position in opponent2Map){
                                        opponent2Pos.add(Pair(position.get("first"),position.get("second")) as Pair<Float, Float>)
                                    }
                                }
                                else if(frame.key == "opponent3"){
                                    var opponent3Map = frame.value as ArrayList<HashMap<String,Float>>
                                    for(position in opponent3Map){
                                        opponent3Pos.add(Pair(position.get("first"),position.get("second")) as Pair<Float, Float>)
                                    }
                                }
                                else if(frame.key == "opponent4"){
                                    var opponent4Map = frame.value as ArrayList<HashMap<String,Float>>
                                    for(position in opponent4Map){
                                        opponent4Pos.add(Pair(position.get("first"),position.get("second")) as Pair<Float, Float>)
                                    }
                                }
                                else if(frame.key == "opponent5"){
                                    var opponent5Map = frame.value as ArrayList<HashMap<String,Float>>
                                    for(position in opponent5Map){
                                        opponent5Pos.add(Pair(position.get("first"),position.get("second")) as Pair<Float, Float>)
                                    }
                                }
                            }
                            resetPlayers()
                            movePlayer(player1View,player1Pos)
                            movePlayer(player2View, player2Pos)
                            movePlayer(player3View, player3Pos)
                            movePlayer(player4View, player4Pos)
                            movePlayer(player5View, player5Pos)
                            movePlayer(ballView, ballPos)
                            movePlayer(opponent1View, opponent1Pos)
                            movePlayer(opponent2View, opponent2Pos)
                            movePlayer(opponent3View, opponent3Pos)
                            movePlayer(opponent4View, opponent4Pos)
                            movePlayer(opponent5View, opponent5Pos)

                        }
                        .addOnFailureListener { e -> Log.w(TAG, "No such document", e) }
                }
            }
        }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }


    /*
 * this method takes as an argument: a player (or ball) and the arraylist of all its points during the tactic
 * it then animates the player passed as argument from point to point in the sequence provided in the arraylist
  */
    private fun movePlayer(player:Button,positions:ArrayList<Pair<Float,Float>>){
        var id = 1
        val animEnd: AnimatorListenerAdapter = object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                id++
                super.onAnimationEnd(animation)
                if (id < positions.size) {
                    player.animate().x(positions.get(id).first).y(positions.get(id).second).setDuration(2200).setListener(this)
                }
            }

        }
        player.animate().x(positions.get(id).first).y(positions.get(id).second).setDuration(2200).setListener(animEnd)

    }

    /*
 * this method clears all of the playerPos arrays
 * used when going back to previous activity to ensure that they are re-setted each time the player loads create tactic activity
  */
    fun clearPlayerPosArrays(){
        player1Pos.clear()
        player2Pos.clear()
        player3Pos.clear()
        player5Pos.clear()
        player4Pos.clear()
        ballPos.clear()
        opponent1Pos.clear()
        opponent2Pos.clear()
        opponent3Pos.clear()
        opponent4Pos.clear()
        opponent5Pos.clear()
    }

    /*
    * this method resets the position of the player provided as argument to the initial one
    * it is used before previewing the tactic
    */
    private fun resetPlayers(){
        try {
            player1View.setX(player1Pos.get(0).first)
            player1View.setY(player1Pos.get(0).second)

            player2View.setX(player2Pos.get(0).first)
            player2View.setY(player2Pos.get(0).second)

            player3View.setX(player3Pos.get(0).first)
            player3View.setY(player3Pos.get(0).second)

            player4View.setX(player4Pos.get(0).first)
            player4View.setY(player4Pos.get(0).second)

            player5View.setX(player5Pos.get(0).first)
            player5View.setY(player5Pos.get(0).second)

            ballView.setX(ballPos.get(0).first)
            ballView.setY(ballPos.get(0).second)

            opponent1View.setX(opponent1Pos.get(0).first)
            opponent1View.setY(opponent1Pos.get(0).second)

            opponent2View.setX(opponent2Pos.get(0).first)
            opponent2View.setY( opponent2Pos.get(0).second)

            opponent3View.setX(opponent3Pos.get(0).first)
            opponent3View.setY(opponent3Pos.get(0).second)

            opponent4View.setX(opponent4Pos.get(0).first)
            opponent4View.setY(opponent4Pos.get(0).second)

            opponent5View.setX(opponent5Pos.get(0).first)
            opponent5View.setY(opponent5Pos.get(0).second)
        }catch (e : Exception){
            e.printStackTrace()
            Toast.makeText(this,"No frames captured!",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}