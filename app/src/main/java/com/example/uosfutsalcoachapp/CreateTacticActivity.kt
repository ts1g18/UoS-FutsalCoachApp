package com.example.uosfutsalcoachapp

import android.content.ClipData
import android.content.ClipDescription
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
import kotlinx.android.synthetic.main.activity_create_tactic.*

class CreateTacticActivity : AppCompatActivity() {
    var yCoord : Float = 0f
    var xCoord : Float = 0f
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_tactic)
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
//                xCoord = event.x
//                yCoord = event.y
                view.invalidate()

                val v = event.localState as View
                v.setX(event.getX())
                v.setY(event.getY())
                v.visibility = View.VISIBLE
                true
            }
            DragEvent.ACTION_DRAG_ENDED ->{
                view.invalidate()
                // Does a getResult(), and displays what happened.
                when(event.result) {
                    true ->
                        Toast.makeText(this, "Player moved.", Toast.LENGTH_LONG)
                    else ->{
                        val v = event.localState as View
                        v.visibility = View.VISIBLE
                        Toast.makeText(this, "Keep the player within the pitch!.", Toast.LENGTH_LONG)
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

}