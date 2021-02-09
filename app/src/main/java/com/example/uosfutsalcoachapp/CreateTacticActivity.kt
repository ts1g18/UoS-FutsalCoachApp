package com.example.uosfutsalcoachapp

import android.R.id
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.ClipDescription
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.DragEvent
import android.view.DragEvent.ACTION_DRAG_ENDED
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_create_tactic.*
import java.lang.Exception


class CreateTacticActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var docRef : DocumentReference
    private val TAG = "CreateTacticActivity"
    val fStore = Firebase.firestore
    //these coordinates represent x y coordinates of the
    var yCoord : Float = 0f
    var xCoord : Float = 0f
    var currentFrame = Frame()
    var tactic = Tactic()
    var frameCounter : Int = 0
    val player1Pos = ArrayList<Pair<Float, Float>>()
    val player2Pos = ArrayList<Pair<Float, Float>>()
    val player3Pos = ArrayList<Pair<Float, Float>>()
    val player4Pos = ArrayList<Pair<Float, Float>>()
    val player5Pos = ArrayList<Pair<Float, Float>>()
    val opponent1Pos = ArrayList<Pair<Float, Float>>()
    val opponent2Pos = ArrayList<Pair<Float, Float>>()
    val opponent3Pos = ArrayList<Pair<Float, Float>>()
    val opponent4Pos = ArrayList<Pair<Float, Float>>()
    val opponent5Pos = ArrayList<Pair<Float, Float>>()
    var clonePlayer1 = ArrayList<Pair<Float,Float>>()
    var clonePlayer2 = ArrayList<Pair<Float,Float>>()
    var clonePlayer3 = ArrayList<Pair<Float,Float>>()
    var clonePlayer4 = ArrayList<Pair<Float,Float>>()
    var clonePlayer5 = ArrayList<Pair<Float,Float>>()
    var cloneBall = ArrayList<Pair<Float,Float>>()
    var cloneOpponent1 = ArrayList<Pair<Float,Float>>()
    var cloneOpponent2 = ArrayList<Pair<Float,Float>>()
    var cloneOpponent3 = ArrayList<Pair<Float,Float>>()
    var cloneOpponent4 = ArrayList<Pair<Float,Float>>()
    var cloneOpponent5 = ArrayList<Pair<Float,Float>>()
    val ballPos = ArrayList<Pair<Float,Float>>()
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_tactic)
        //futsal pitch is the boundaries on which we can drag
        futsal_pitch.setOnDragListener(dragListener)

        //call the action bar
        val actionBar = supportActionBar
        //show the back button in action bar
        if(actionBar != null){
            //set action bar title
                actionBar!!.title = "Create Tactic"
            //set back button
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        val player1 : Button = findViewById(R.id.player1)
        player1.setOnLongClickListener{
            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(null, dragShadowBuilder, it, 0)
            it.visibility = View.INVISIBLE
            true
        }
        val player2 : Button = findViewById(R.id.player2)
        player2.setOnLongClickListener{
            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(null, dragShadowBuilder, it, 0)
            it.visibility = View.INVISIBLE
            true
        }
        val player3 : Button = findViewById(R.id.player3)
        player3.setOnLongClickListener{
            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(null, dragShadowBuilder, it, 0)

            it.visibility = View.INVISIBLE
            true
        }
        val player4 : Button = findViewById(R.id.player4)
        player4.setOnLongClickListener{
            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(null, dragShadowBuilder, it, 0)

            it.visibility = View.INVISIBLE
            true
        }
        val player5 : Button = findViewById(R.id.player5)
        player5.setOnLongClickListener{
            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(null, dragShadowBuilder, it, 0)
            it.visibility = View.INVISIBLE
            true
        }
        val ball : Button = findViewById(R.id.ball)
        ball.setOnLongClickListener{
            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(null, dragShadowBuilder, it, 0)
            it.visibility = View.INVISIBLE
            true
        }
        val opponent1 : Button = findViewById(R.id.opponent1)
        opponent1.setOnLongClickListener{
            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(null, dragShadowBuilder, it, 0)
            it.visibility = View.INVISIBLE
            true
        }
        val opponent2 : Button = findViewById(R.id.opponent2)
        opponent2.setOnLongClickListener{
            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(null, dragShadowBuilder, it, 0)
            it.visibility = View.INVISIBLE
            true
        }
        val opponent3 : Button = findViewById(R.id.opponent3)
        opponent3.setOnLongClickListener{
            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(null, dragShadowBuilder, it, 0)
            it.visibility = View.INVISIBLE
            true
        }
        val opponent4 : Button = findViewById(R.id.opponent4)
        opponent4.setOnLongClickListener{
            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(null, dragShadowBuilder, it, 0)
            it.visibility = View.INVISIBLE
            true
        }
        val opponent5 : Button = findViewById(R.id.opponent5)
        opponent5.setOnLongClickListener{
            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(null, dragShadowBuilder, it, 0)
            it.visibility = View.INVISIBLE
            true
        }

        val captureFrameBtn : Button = findViewById(R.id.btnCaptureFrame)
        captureFrameBtn.setOnClickListener{
            storePlayerPos()
            //clone playerPositions array to avoid problem with reference overwriting existing values
            val clone = clone(currentFrame.getFrame())
            tactic.setTactic(frameCounter, clone)
            Toast.makeText(this, "Frame $frameCounter was captured.", Toast.LENGTH_SHORT).show()
            frameCounter++
        }
        val previewTacticBtn : Button = findViewById(R.id.btnPreviewTactic)
        previewTacticBtn.setOnClickListener {
            try {
                generateListOfPlayerPositions()
                //first we need to set the position of the players to the initial one
                resetPlayers()
                //move the players to demonstrate tactic
                movePlayer(player1, player1Pos)
                movePlayer(player2, player2Pos)
                movePlayer(player3, player3Pos)
                movePlayer(player4, player4Pos)
                movePlayer(player5, player5Pos)
                movePlayer(ball, ballPos)
                movePlayer(opponent1, opponent1Pos)
                movePlayer(opponent2, opponent2Pos)
                movePlayer(opponent3, opponent3Pos)
                movePlayer(opponent4, opponent4Pos)
                movePlayer(opponent5, opponent5Pos)
            } catch (e: Exception) {

            }
        }

        val saveTacticBtn : Button = findViewById(R.id.btnSaveTactic)
        saveTacticBtn.setOnClickListener {
            createPopup()
        }
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
                }else{
                    clearPlayerPosArrays()
                }
            }

        }
        player.animate().x(positions.get(id).first).y(positions.get(id).second).setDuration(2200).setListener(animEnd)

    }
    /*
     * this method resets the position of the player provided as argument to the initial one
     * it is used before previewing the tactic
     */
    private fun resetPlayers(){
        try {
            player1.setX(player1Pos.get(0).first)
            player1.setY(player1Pos.get(0).second)

            player2.setX(player2Pos.get(0).first)
            player2.setY(player2Pos.get(0).second)

            player3.setX(player3Pos.get(0).first)
            player3.setY(player3Pos.get(0).second)

            player4.setX(player4Pos.get(0).first)
            player4.setY(player4Pos.get(0).second)

            player5.setX(player5Pos.get(0).first)
            player5.setY(player5Pos.get(0).second)

            ball.setX(ballPos.get(0).first)
            ball.setY(ballPos.get(0).second)

            opponent1.setX(opponent1Pos.get(0).first)
            opponent1.setY(opponent1Pos.get(0).second)

            opponent2.setX(opponent2Pos.get(0).first)
            opponent2.setY(opponent2Pos.get(0).second)

            opponent3.setX(opponent3Pos.get(0).first)
            opponent3.setY(opponent3Pos.get(0).second)

            opponent4.setX(opponent4Pos.get(0).first)
            opponent4.setY(opponent4Pos.get(0).second)

            opponent5.setX(opponent5Pos.get(0).first)
            opponent5.setY(opponent5Pos.get(0).second)
        }catch (e : Exception){
            Toast.makeText(this,"No frames captured!",Toast.LENGTH_SHORT).show()
        }
    }
    private val dragListener = View.OnDragListener{ view, event ->
        when(event.action){
            //in response to the user gesture to begin drag (long clin on player pin)
                //the event with action type 'ACTION_DRAG_STARTED' should have a listener that uses the MIME type methods in clip descritpion
                    //if the listener can accept a drop, it should return true to tell the system to continue to send drag events to the listener
                        //if it returns false the system will stop sending drag events until it sends out 'ACTION_DRAG_ENDED'
            DragEvent.ACTION_DRAG_STARTED -> {
                true
            }
            //the listener receives 'ACTION_DRAG_ENTERED' when the touch point (point on screen underneath user's finger) has entered the bounding box of the listeners' View
            DragEvent.ACTION_DRAG_ENTERED -> {
                view.invalidate()
                true
            }
            //
            DragEvent.ACTION_DRAG_LOCATION -> {
                true
            }
            //when drag view leaves our boundaries
            DragEvent.ACTION_DRAG_EXITED -> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DROP -> {
                xCoord = event.x
                yCoord = event.y

                view.invalidate()

                val v = event.localState as View
                //need to substract half of the image's width from X and half of its height from Y in order to ensure that player pin is droped exactly where the user wants
                v.setX(xCoord - (v.width / 2))
                v.setY(yCoord - (v.width / 2))
                v.visibility = View.VISIBLE

                true
            }
            ACTION_DRAG_ENDED -> {
                view.invalidate()

                // Does a getResult(), and displays what happened.
                when (event.result) {
                    true ->
                        Toast.makeText(this, "Player moved.", Toast.LENGTH_SHORT)
                    else -> {
                        val v = event.localState as View
                        v.visibility = View.VISIBLE
                        Toast.makeText(this, "Keep the player within the pitch!", Toast.LENGTH_SHORT)
                    }

                }.show()

                true
            }
            else -> false
        }
    }
    override fun onBackPressed() {
            AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Go Back?")
                    .setMessage("Are you sure you want to go back?")
                    .setPositiveButton("Yes") { dialog, which -> finish()
                        if(frameCounter>0) {
                            Toast.makeText(this, "Tactic was not saved.", Toast.LENGTH_SHORT).show()
                            tactic.getTactic().clear()
                            currentFrame.getFrame().clear()
                            clearPlayerPosArrays()
                        }
                    }
                    .setNegativeButton("No", null)
                    .show()
    }
    //when back is pressed go to previous activity
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    /*
    * this method will be used to check which player pin is being moved and change the coordinates of that specific player in the hashmap with all player positions
    * it stores the position of all the player
     */
    private fun storePlayerPos(){
        val player1Coord = Pair(player1.x, player1.y)
        val player2Coord = Pair(player2.x, player2.y)
        val player3Coord = Pair(player3.x, player3.y)
        val player4Coord = Pair(player4.x, player4.y)
        val player5Coord = Pair(player5.x, player5.y)
        val ballCoord = Pair(ball.x, ball.y)
        val opponent1Coord = Pair(opponent1.x, opponent1.y)
        val opponent2Coord = Pair(opponent2.x, opponent2.y)
        val opponent3Coord = Pair(opponent3.x, opponent3.y)
        val opponent4Coord = Pair(opponent4.x, opponent4.y)
        val opponent5Coord = Pair(opponent5.x, opponent5.y)
        currentFrame.setFrame(player1, player1Coord)
        currentFrame.setFrame(player2, player2Coord)
        currentFrame.setFrame(player3, player3Coord)
        currentFrame.setFrame(player4, player4Coord)
        currentFrame.setFrame(player5, player5Coord)
        currentFrame.setFrame(ball, ballCoord)
        currentFrame.setFrame(opponent1, opponent1Coord)
        currentFrame.setFrame(opponent2, opponent2Coord)
        currentFrame.setFrame(opponent3, opponent3Coord)
        currentFrame.setFrame(opponent4, opponent4Coord)
        currentFrame.setFrame(opponent5, opponent5Coord)
    }

    /*
    * this method loops through the tactic (frames mapped to (player,position) ) stores the position of each player in the appropriate arraylist in sequence
    * E.g. Stores in player1Pos arraylist the position of player 1 in frame 0 at index 0, frame 1 at index 1 etc.
     */
    private fun generateListOfPlayerPositions(){
        tactic.getTactic().forEach{ (frameNumber, frame)->
            val frame = tactic.getTactic().get(frameNumber)
            frame?.forEach { (player, playerPos) ->
                if(player.toString().contains("player1")){
                    frame.get(player)?.let { it1 -> player1Pos.add(it1) }
                } else if(player.toString().contains("player2")){
                    frame.get(player)?.let { it1 -> player2Pos.add(it1) }
                }
                else if(player.toString().contains("player3")){
                    frame.get(player)?.let { it1 -> player3Pos.add(it1) }
                }
                else if(player.toString().contains("player4")){
                    frame.get(player)?.let { it1 -> player4Pos.add(it1) }
                }
                else if(player.toString().contains("player5")){
                    frame.get(player)?.let { it1 -> player5Pos.add(it1) }
                }else if(player.toString().contains("ball")){
                    frame.get(player)?.let { it1 -> ballPos.add(it1) }
                }else if(player.toString().contains("opponent1")){
                    frame.get(player)?.let { it1 -> opponent1Pos.add(it1) }
                }
                else if(player.toString().contains("opponent2")){
                frame.get(player)?.let { it1 -> opponent2Pos.add(it1) }
                }
                else if(player.toString().contains("opponent3")){
                frame.get(player)?.let { it1 -> opponent3Pos.add(it1) }
                }
                else if(player.toString().contains("opponent4")) {
                    frame.get(player)?.let { it1 -> opponent4Pos.add(it1) }
                }
                else if(player.toString().contains("opponent5")) {
                    frame.get(player)?.let { it1 -> opponent5Pos.add(it1) }
                }
            }
        }
        clonePlayerPos()
    }
    /*
    * this method is used to clone the playerPositions hashmap before storing in the tactics hashmap
    * this is because when trying to map the playerPositions Hashmap with the frames in the tactics hashmap the previous values in previous frames were overwritten
    * this was because the values in the tactics hashmap were a reference to the playerPositions hashmap and any changes in that map reflected changes in the tactics map
     */
    fun <K, V> clone(original: Map<K, V>): HashMap<K, V> {
        val copy: HashMap<K, V> = HashMap()
        copy.putAll(original)
        return copy
    }

    /*
    * this method stores the tactic in the firestore database when clicking the save tactic button.
     */
    private fun saveTactic(tacticName : String){
        val tactic = hashMapOf(
            "player1" to clonePlayer1,
            "player2" to clonePlayer2,
            "player3" to clonePlayer3,
            "player4" to clonePlayer4,
            "player5" to clonePlayer5,
            "ball" to cloneBall,
            "opponent1" to cloneOpponent1,
            "opponent2" to cloneOpponent2,
            "opponent3" to cloneOpponent3,
            "opponent4" to cloneOpponent4,
            "opponent5" to cloneOpponent5,

        )

        val documentReference : DocumentReference = fStore.collection("tactics").document(tacticName)
        // Add a new document with a generated ID
        documentReference.set(tactic)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    private fun createPopup(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Save Tactic")

        val view = layoutInflater.inflate(R.layout.layout_popup,null)
        val tacticName = view.findViewById<EditText>(R.id.tactic_name)

        builder.setView((view))
        builder.setPositiveButton("Save", DialogInterface.OnClickListener{ _, _ ->
            if(tacticName.text.toString().isEmpty()){
                Toast.makeText(this,"Please enter a name for the tactic",Toast.LENGTH_SHORT).show()
            }else {
                saveTactic(tacticName.text.toString())
                Toast.makeText(this, "Tactic was saved successfuly!",Toast.LENGTH_SHORT).show()
                clearPlayerPosArrays()
                tactic.getTactic().clear()
                currentFrame.getFrame().clear()
                startActivity(Intent(this, HomeScreenActivity::class.java))
                finish()
            }
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener{ _, _ -> })
        builder.show()
    }

    /*
    * clones all of the PlayerPos arrays so that they can be stored to database
     */
    fun clonePlayerPos(){
        clonePlayer1 = ArrayList(player1Pos)
        clonePlayer2 = ArrayList(player2Pos)
        clonePlayer3 = ArrayList(player3Pos)
        clonePlayer4 = ArrayList(player4Pos)
        clonePlayer5 = ArrayList(player5Pos)
        cloneBall  = ArrayList(ballPos)
        cloneOpponent1 = ArrayList(opponent1Pos)
        cloneOpponent2 = ArrayList(opponent2Pos)
        cloneOpponent3 = ArrayList(opponent3Pos)
        cloneOpponent4 = ArrayList(opponent4Pos)
        cloneOpponent5 = ArrayList(opponent5Pos)
    }

}