package com.example.uosfutsalcoachapp

import android.content.ClipData
import android.content.ClipDescription
import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.DragEvent.ACTION_DRAG_ENDED
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_create_tactic.*

class CreateTacticActivity : AppCompatActivity() {
    //these coordinates represent x y coordinates of the
    var yCoord : Float = 0f
    var xCoord : Float = 0f
    var currentFrame = Frame()
    var tactic = Tactic()
    var frameCounter : Int = 0
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
                actionBar!!.title = "Home Screen Activiy"
            //set back button
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        val player1 : Button = findViewById(R.id.player1)
        player1.setOnLongClickListener{
            val clipText = "This is our ClipData text"
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)

            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, dragShadowBuilder, it, 0)

            it.visibility = View.INVISIBLE
            true
        }
        val player2 : Button = findViewById(R.id.player2)
        player2.setOnLongClickListener{
            val clipText = "This is our ClipData text"
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)

            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, dragShadowBuilder, it, 0)

            it.visibility = View.INVISIBLE
            true
        }
        val player3 : Button = findViewById(R.id.player3)
        player3.setOnLongClickListener{
            val clipText = "This is our ClipData text"
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)

            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, dragShadowBuilder, it, 0)

            it.visibility = View.INVISIBLE
            true
        }

        val captureFrameBtn : Button = findViewById(R.id.btnCaptureFrame)
        captureFrameBtn.setOnClickListener{
            storePlayerPos()
            val clone = clone(currentFrame.getFrame())
            tactic.setTactic(frameCounter, clone)
            println("HEYY ${tactic.getTactic().size}")
            Toast.makeText(this, "Frame $frameCounter was captured.", Toast.LENGTH_SHORT).show()
            frameCounter++
        }

        val previewTacticBtn : Button = findViewById(R.id.btnPreviewTactic)
        previewTacticBtn.setOnClickListener{
        }

    }

    val dragListener = View.OnDragListener{ view, event ->
        when(event.action){
            //in response to the user gesture to begin drag (long clin on player pin)
                //the event with action type 'ACTION_DRAG_STARTED' should have a listener that uses the MIME type methods in clip descritpion
                    //if the listener can accept a drop, it should return true to tell the system to continue to send drag events to the listener
                        //if it returns false the system will stop sending drag events until it sends out 'ACTION_DRAG_ENDED'
            DragEvent.ACTION_DRAG_STARTED -> {
                event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
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
                val item = event.clipData.getItemAt(0)
                val dragData = item.text
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
                    .setPositiveButton("Yes") {
                        dialog, which -> finish()
                        if(frameCounter>0) {
                            Toast.makeText(this,"Tactic was not saved.",Toast.LENGTH_SHORT).show()
                            tactic.getTactic().clear()
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
    fun storePlayerPos(){
        val player1Coord = Pair(player1.x, player1.y)
        val player2Coord = Pair(player2.x, player2.y)
        val player3Coord = Pair(player3.x, player3.y)
        currentFrame.setFrame(player1, player1Coord)
        currentFrame.setFrame(player2, player2Coord)
        currentFrame.setFrame(player3, player3Coord)
    }
    /*
    * this method loops through each player's array (that contains player's position in each frame) and calculates distance that needs to be covered by each player from 1 frame to the next
     */
//    fun distanceToMove(){
//        println(player1Pos)
//        println(player2Pos)
//        println(player3Pos)
//        for(position in player1Pos){
//            var fromX = player1Pos.get(i).first
//            var fromY = player1Pos.get(i).second
//            i++
//            if(i < player1Pos.size) {
//                var toX = player1Pos.get(i).first
//                var toY = player1Pos.get(i).second
//                println("X from $fromX to $toX")
//                println("Y from $fromY to $toY")
//            }
//
//        }
//    }

    /*
    * this method goes through the 'playerPositions' and stores in an array list (passed as an arguement) the position of a single player at each frame (represented by indexes, e.g. index 0 = frame 0)
     */
    fun getFromPosition(playerArray: ArrayList<Pair<Float, Float>>){
        var fromX = currentFrame.getPlayerXPosition(player1)
        var fromY = currentFrame.getPlayerYPosition(player1)

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
}