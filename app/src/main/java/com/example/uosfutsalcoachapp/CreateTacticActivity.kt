package com.example.uosfutsalcoachapp

import android.animation.ObjectAnimator
import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Point
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Bundle
import android.view.*
import android.view.DragEvent.ACTION_DRAG_ENDED
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isInvisible
import kotlinx.android.synthetic.main.activity_create_tactic.*
import java.util.concurrent.TimeUnit

class CreateTacticActivity : AppCompatActivity() {
    //these coordinates represent x y coordinates of the
    var yCoord : Float = 0f
    var xCoord : Float = 0f
    //playerPositions is hashmap that stores the position of all player pins (button)
    val playerPositions = HashMap<Button,Pair<Float,Float>>()
    //framesList stores the position (x,y) of each player(value) and links it with the current frame
    val framesList = HashMap<Int,HashMap<Button,Pair<Float,Float>>>()
    var i : Int = 0
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
            val data = ClipData(clipText,mimeTypes,item)

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
            val data = ClipData(clipText,mimeTypes,item)

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
            val data = ClipData(clipText,mimeTypes,item)

            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, dragShadowBuilder, it, 0)

            it.visibility = View.INVISIBLE
            true
        }

        val captureFrameBtn : Button = findViewById(R.id.btnCaptureFrame)
        captureFrameBtn.setOnClickListener{
            storePlayerPos()
            framesList.put(i,playerPositions)
            Toast.makeText(this, "Frame $i was captured.", Toast.LENGTH_LONG).show()
            println("kati $i $framesList")
            i++
        }

        val previewTacticBtn : Button = findViewById(R.id.btnPreviewTactic)
        previewTacticBtn.setOnClickListener{
            framesList.forEach{(frame,player) ->
                ObjectAnimator.ofFloat(player1,"translationX",100f).apply {
                    duration = 2000
                    start()
                 }
                ObjectAnimator.ofFloat(player2,"translationX",100f).apply {
                    duration = 2000
                    start()
                }
            }
        }

    }

    val dragListener = View.OnDragListener{view,event ->
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
            DragEvent.ACTION_DROP->{
                val item = event.clipData.getItemAt(0)
                val dragData = item.text
                Toast.makeText(this, dragData, Toast.LENGTH_SHORT).show()
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
            ACTION_DRAG_ENDED ->{
                view.invalidate()


                // Does a getResult(), and displays what happened.
                when(event.result) {
                    true ->
                        Toast.makeText(this, "Player moved.", Toast.LENGTH_SHORT)
                    else ->{
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
        val player1Coord = Pair(player1.x,player1.y)
        val player2Coord = Pair(player2.x,player2.y)
        val player3Coord = Pair(player3.x,player3.y)
            playerPositions.put(player1,player1Coord)
            playerPositions.put(player2,player2Coord)
            playerPositions.put(player3,player3Coord)
    }
    /*
    * this method loops through the framesList hashmap and calculates the distance that needs to be covered by each player in each frame
     */
    fun distanceToMove(){
        
    }
}