package com.example.uosfutsalcoachapp

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_create_tactic.*
import kotlinx.android.synthetic.main.activity_edit_tactic.*
import kotlinx.android.synthetic.main.activity_profile.*

class EditTacticActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private var tacticName = ""
    private var frame1 = 0
    var player1Pos = ArrayList<Pair<Float, Float>>()
    var player2Pos = ArrayList<Pair<Float, Float>>()
    var player3Pos = ArrayList<Pair<Float, Float>>()
    var player4Pos = ArrayList<Pair<Float, Float>>()
    var player5Pos = ArrayList<Pair<Float, Float>>()
    var ballPos = ArrayList<Pair<Float, Float>>()
    var opponent1Pos = ArrayList<Pair<Float, Float>>()
    var opponent2Pos = ArrayList<Pair<Float, Float>>()
    var opponent3Pos = ArrayList<Pair<Float, Float>>()
    var opponent4Pos = ArrayList<Pair<Float, Float>>()
    var opponent5Pos = ArrayList<Pair<Float, Float>>()
    private val TAG = "TacticActivity"
    var screenRatioHeight = 0f
    var screenRatioWidth = 0f
    var diffScreenSize = false
    var currentFrame = 0

    //these coordinates represent x y coordinates of the
    var yCoord: Float = 0f
    var xCoord: Float = 0f
    var deviceWidth = 0f
    var deviceHeight = 0f
    val displayMetrics = DisplayMetrics()
    var frameToDeleteFromMenu = ""


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_tactic)

        futsal_pitch_edit.setOnDragListener(dragListener)

        val windowsManager = applicationContext.getSystemService(WINDOW_SERVICE) as WindowManager
        windowsManager.defaultDisplay.getMetrics(displayMetrics)
        deviceWidth = displayMetrics.widthPixels.toFloat()
        deviceHeight = displayMetrics.heightPixels.toFloat()

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


        getTacticData()

        val player1: Button = findViewById(R.id.player1Edit)
        player1.setOnLongClickListener {
            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(null, dragShadowBuilder, it, 0)
            it.visibility = View.INVISIBLE
            true
        }
        val player2: Button = findViewById(R.id.player2Edit)
        player2.setOnLongClickListener {
            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(null, dragShadowBuilder, it, 0)
            it.visibility = View.INVISIBLE
            true
        }
        val player3: Button = findViewById(R.id.player3Edit)
        player3.setOnLongClickListener {
            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(null, dragShadowBuilder, it, 0)

            it.visibility = View.INVISIBLE
            true
        }
        val player4: Button = findViewById(R.id.player4Edit)
        player4.setOnLongClickListener {
            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(null, dragShadowBuilder, it, 0)

            it.visibility = View.INVISIBLE
            true
        }
        val player5: Button = findViewById(R.id.player5Edit)
        player5.setOnLongClickListener {
            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(null, dragShadowBuilder, it, 0)
            it.visibility = View.INVISIBLE
            true
        }
        val ball: Button = findViewById(R.id.ballEdit)
        ball.setOnLongClickListener {
            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(null, dragShadowBuilder, it, 0)
            it.visibility = View.INVISIBLE
            true
        }
        val opponent1: Button = findViewById(R.id.opponent1Edit)
        opponent1.setOnLongClickListener {
            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(null, dragShadowBuilder, it, 0)
            it.visibility = View.INVISIBLE
            true
        }
        val opponent2: Button = findViewById(R.id.opponent2Edit)
        opponent2.setOnLongClickListener {
            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(null, dragShadowBuilder, it, 0)
            it.visibility = View.INVISIBLE
            true
        }
        val opponent3: Button = findViewById(R.id.opponent3Edit)
        opponent3.setOnLongClickListener {
            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(null, dragShadowBuilder, it, 0)
            it.visibility = View.INVISIBLE
            true
        }
        val opponent4: Button = findViewById(R.id.opponent4Edit)
        opponent4.setOnLongClickListener {
            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(null, dragShadowBuilder, it, 0)
            it.visibility = View.INVISIBLE
            true
        }
        val opponent5: Button = findViewById(R.id.opponent5Edit)
        opponent5.setOnLongClickListener {
            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(null, dragShadowBuilder, it, 0)
            it.visibility = View.INVISIBLE
            true
        }

        val btnUpdateFrame: Button = findViewById(R.id.btnUpdateFrame)
        btnUpdateFrame.setOnClickListener {
            changePosOfAllPlayersInFrame()
        }

        val btnSaveTacticEdit: Button = findViewById(R.id.btnSaveTacticEdit)
        btnSaveTacticEdit.setOnClickListener {
            createPopup()
        }

        val btnDeleteFrame: Button = findViewById(R.id.btnDeleteFrame)
        btnDeleteFrame.setOnClickListener {
            deleteCurrentFrame()
        }

        val btnAddFrame: Button = findViewById(R.id.btnAddFrame)
        btnAddFrame.setOnClickListener {
            addFrame()
        }

    }
    /*
    * this method adds a frame in the tactic
    * the captain has to first drag and drop the players where he wants on the pitch
    * then by clicking on the add frame button the current positions of all players are stored after the currently selected frame
     */
    private fun addFrame() {
        var newPlayer1Pos = Pair(player1Edit.getX(), player1Edit.getY())
        player1Pos.add(currentFrame, newPlayer1Pos)

        var newPlayer2Pos = Pair(player2Edit.getX(), player2Edit.getY())
        player2Pos.add(currentFrame, newPlayer2Pos)

        var newPlayer3Pos = Pair(player3Edit.getX(), player3Edit.getY())
        player3Pos.add(currentFrame, newPlayer3Pos)

        var newPlayer4Pos = Pair(player4Edit.getX(), player4Edit.getY())
        player4Pos.add(currentFrame, newPlayer4Pos)

        var newPlayer5Pos = Pair(player5Edit.getX(), player5Edit.getY())
        player5Pos.add(currentFrame, newPlayer5Pos)

        var newBallPos = Pair(ballEdit.getX(), ballEdit.getY())
        ballPos.add(currentFrame, newBallPos)

        var newOpponent1Pos = Pair(opponent1Edit.getX(), opponent1Edit.getY())
        opponent1Pos.add(currentFrame, newOpponent1Pos)

        var newOpponent2Pos = Pair(opponent2Edit.getX(), opponent2Edit.getY())
        opponent2Pos.add(currentFrame, newOpponent2Pos)

        var newOpponent3Pos = Pair(opponent3Edit.getX(), opponent3Edit.getY())
        opponent3Pos.add(currentFrame, newOpponent3Pos)

        var newOpponent4Pos = Pair(opponent4Edit.getX(), opponent4Edit.getY())
        opponent4Pos.add(currentFrame, newOpponent4Pos)

        var newOpponent5Pos = Pair(opponent5Edit.getX(), opponent5Edit.getY())
        opponent5Pos.add(currentFrame, newOpponent5Pos)
    }

    /*
    * this method changes the position of the players in the current frame (updates the frame)
     */
    private fun changePosOfAllPlayersInFrame() {
        var newPlayer1Pos = Pair(player1Edit.getX(), player1Edit.getY())
        player1Pos.set(currentFrame, newPlayer1Pos)

        var newPlayer2Pos = Pair(player2Edit.getX(), player2Edit.getY())
        player2Pos.set(currentFrame, newPlayer2Pos)

        var newPlayer3Pos = Pair(player3Edit.getX(), player3Edit.getY())
        player3Pos.set(currentFrame, newPlayer3Pos)

        var newPlayer4Pos = Pair(player4Edit.getX(), player4Edit.getY())
        player4Pos.set(currentFrame, newPlayer4Pos)

        var newPlayer5Pos = Pair(player5Edit.getX(), player5Edit.getY())
        player5Pos.set(currentFrame, newPlayer5Pos)

        var newBallPos = Pair(ballEdit.getX(), ballEdit.getY())
        ballPos.set(currentFrame, newBallPos)

        var newOpponent1Pos = Pair(opponent1Edit.getX(), opponent1Edit.getY())
        opponent1Pos.set(currentFrame, newOpponent1Pos)

        var newOpponent2Pos = Pair(opponent2Edit.getX(), opponent2Edit.getY())
        opponent2Pos.set(currentFrame, newOpponent2Pos)

        var newOpponent3Pos = Pair(opponent3Edit.getX(), opponent3Edit.getY())
        opponent3Pos.set(currentFrame, newOpponent3Pos)

        var newOpponent4Pos = Pair(opponent4Edit.getX(), opponent4Edit.getY())
        opponent4Pos.set(currentFrame, newOpponent4Pos)

        var newOpponent5Pos = Pair(opponent5Edit.getX(), opponent5Edit.getY())
        opponent5Pos.set(currentFrame, newOpponent5Pos)
    }

    /*
    * this method saves the updated tactic (updated frames) in the firestore
     */
    private fun createPopup() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Save Tactic")

        val view = layoutInflater.inflate(R.layout.layout_popup_edit, null)
        val tacticName = view.findViewById<EditText>(R.id.tactic_name_edit)
        tacticName.setText(this.tacticName)

        builder.setView((view))
        builder.setPositiveButton("Save", DialogInterface.OnClickListener { _, _ ->
            if (tacticName.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter a name for the tactic", Toast.LENGTH_SHORT)
                    .show()
            } else {
                saveTactic(tacticName.text.toString())
                Toast.makeText(this, "Tactic was updated successfuly!", Toast.LENGTH_SHORT).show()
                clearPlayerPosArrays()
                startActivity(Intent(this, HomeScreenActivity::class.java))
                finish()
            }
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ -> })
        builder.show()
    }

    /*
    * this method stores the tactic in the firestore database when clicking the save tactic button.
    */
    private fun saveTactic(updatedTacticName: String) {
        val updatedTactic = hashMapOf(
            "screenSizeCreator" to Pair(deviceHeight, deviceWidth),
            "player1" to player1Pos,
            "player2" to player2Pos,
            "player3" to player3Pos,
            "player4" to player4Pos,
            "player5" to player5Pos,
            "ball" to ballPos,
            "opponent1" to opponent1Pos,
            "opponent2" to opponent2Pos,
            "opponent3" to opponent3Pos,
            "opponent4" to opponent4Pos,
            "opponent5" to opponent5Pos,

            )

        val documentReference: DocumentReference = fStore.collection("tactics")
            .document(updatedTacticName) //needs to overwrite current tactic
        if (updatedTacticName == tacticName) {
            // Add a new document with a generated ID
            documentReference.set(updatedTactic)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        } else {
            deleteOldTactic()
            // Add a new document with a generated ID
            documentReference.set(updatedTactic)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
    }

    /*
    * this method deletes the old tactic
     */
    private fun deleteOldTactic() {
        fStore.collection("tactics").get().addOnSuccessListener { result ->
            for (document in result) {
                if (document.id == tacticName) {
                    fStore.collection("tactics").document(tacticName).delete()
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
    }

    private fun deleteCurrentFrame() {
        player1Pos.removeAt(currentFrame)
        player2Pos.removeAt(currentFrame)
        player3Pos.removeAt(currentFrame)
        player4Pos.removeAt(currentFrame)
        player5Pos.removeAt(currentFrame)
        ballPos.removeAt(currentFrame)
        opponent1Pos.removeAt(currentFrame)
        opponent2Pos.removeAt(currentFrame)
        opponent3Pos.removeAt(currentFrame)
        opponent4Pos.removeAt(currentFrame)
        opponent5Pos.removeAt(currentFrame)
        if(currentFrame < player1Pos.size) {
            setPlayersPosPerFrame(player1Edit, player1Pos, currentFrame)
            setPlayersPosPerFrame(player2Edit, player2Pos, currentFrame)
            setPlayersPosPerFrame(player3Edit, player3Pos, currentFrame)
            setPlayersPosPerFrame(player4Edit, player4Pos, currentFrame)
            setPlayersPosPerFrame(player5Edit, player5Pos, currentFrame)
            setPlayersPosPerFrame(ballEdit, ballPos, currentFrame)
            setPlayersPosPerFrame(opponent1Edit, opponent1Pos, currentFrame)
            setPlayersPosPerFrame(opponent2Edit, opponent2Pos, currentFrame)
            setPlayersPosPerFrame(opponent3Edit, opponent3Pos, currentFrame)
            setPlayersPosPerFrame(opponent4Edit, opponent4Pos, currentFrame)
            setPlayersPosPerFrame(opponent5Edit, opponent5Pos, currentFrame)
        }else{
            setPlayersPosPerFrame(player1Edit, player1Pos, currentFrame-1)
            setPlayersPosPerFrame(player2Edit, player2Pos, currentFrame-1)
            setPlayersPosPerFrame(player3Edit, player3Pos, currentFrame-1)
            setPlayersPosPerFrame(player4Edit, player4Pos, currentFrame-1)
            setPlayersPosPerFrame(player5Edit, player5Pos, currentFrame-1)
            setPlayersPosPerFrame(ballEdit, ballPos, currentFrame-1)
            setPlayersPosPerFrame(opponent1Edit, opponent1Pos, currentFrame-1)
            setPlayersPosPerFrame(opponent2Edit, opponent2Pos, currentFrame-1)
            setPlayersPosPerFrame(opponent3Edit, opponent3Pos, currentFrame-1)
            setPlayersPosPerFrame(opponent4Edit, opponent4Pos, currentFrame-1)
            setPlayersPosPerFrame(opponent5Edit, opponent5Pos, currentFrame-1)
        }
    }


    private fun setPlayersPosPerFrame(
        player: Button,
        playerPosList: ArrayList<Pair<Float, Float>>,
        frame: Int
    ) {
        player.setX(playerPosList.get(frame).first)
        player.setY(playerPosList.get(frame).second)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var itemTitle = item.title
        try {
            when (itemTitle) {
                "Frame 0" -> {
                    currentFrame = 0
                    setPlayersPosPerFrame(player1Edit, player1Pos, 0)
                    setPlayersPosPerFrame(player2Edit, player2Pos, 0)
                    setPlayersPosPerFrame(player3Edit, player3Pos, 0)
                    setPlayersPosPerFrame(player4Edit, player4Pos, 0)
                    setPlayersPosPerFrame(player5Edit, player5Pos, 0)
                    setPlayersPosPerFrame(ballEdit, ballPos, 0)
                    setPlayersPosPerFrame(opponent1Edit, opponent1Pos, 0)
                    setPlayersPosPerFrame(opponent2Edit, opponent2Pos, 0)
                    setPlayersPosPerFrame(opponent3Edit, opponent3Pos, 0)
                    setPlayersPosPerFrame(opponent4Edit, opponent4Pos, 0)
                    setPlayersPosPerFrame(opponent5Edit, opponent5Pos, 0)
                    return true
                }
                "Frame 1" -> {
                    currentFrame = 1
                    setPlayersPosPerFrame(player1Edit, player1Pos, 1)
                    setPlayersPosPerFrame(player2Edit, player2Pos, 1)
                    setPlayersPosPerFrame(player3Edit, player3Pos, 1)
                    setPlayersPosPerFrame(player4Edit, player4Pos, 1)
                    setPlayersPosPerFrame(player5Edit, player5Pos, 1)
                    setPlayersPosPerFrame(ballEdit, ballPos, 1)
                    setPlayersPosPerFrame(opponent1Edit, opponent1Pos, 1)
                    setPlayersPosPerFrame(opponent2Edit, opponent2Pos, 1)
                    setPlayersPosPerFrame(opponent3Edit, opponent3Pos, 1)
                    setPlayersPosPerFrame(opponent4Edit, opponent4Pos, 1)
                    setPlayersPosPerFrame(opponent5Edit, opponent5Pos, 1)
                    return true
                }
                "Frame 2" -> {
                    currentFrame = 2
                    setPlayersPosPerFrame(player1Edit, player1Pos, 2)
                    setPlayersPosPerFrame(player2Edit, player2Pos, 2)
                    setPlayersPosPerFrame(player3Edit, player3Pos, 2)
                    setPlayersPosPerFrame(player4Edit, player4Pos, 2)
                    setPlayersPosPerFrame(player5Edit, player5Pos, 2)
                    setPlayersPosPerFrame(ballEdit, ballPos, 2)
                    setPlayersPosPerFrame(opponent1Edit, opponent1Pos, 2)
                    setPlayersPosPerFrame(opponent2Edit, opponent2Pos, 2)
                    setPlayersPosPerFrame(opponent3Edit, opponent3Pos, 2)
                    setPlayersPosPerFrame(opponent4Edit, opponent4Pos, 2)
                    setPlayersPosPerFrame(opponent5Edit, opponent5Pos, 2)
                    return true
                }
                "Frame 3" -> {
                    currentFrame = 3
                    setPlayersPosPerFrame(player1Edit, player1Pos, 3)
                    setPlayersPosPerFrame(player2Edit, player2Pos, 3)
                    setPlayersPosPerFrame(player3Edit, player3Pos, 3)
                    setPlayersPosPerFrame(player4Edit, player4Pos, 3)
                    setPlayersPosPerFrame(player5Edit, player5Pos, 3)
                    setPlayersPosPerFrame(ballEdit, ballPos, 3)
                    setPlayersPosPerFrame(opponent1Edit, opponent1Pos, 3)
                    setPlayersPosPerFrame(opponent2Edit, opponent2Pos, 3)
                    setPlayersPosPerFrame(opponent3Edit, opponent3Pos, 3)
                    setPlayersPosPerFrame(opponent4Edit, opponent4Pos, 3)
                    setPlayersPosPerFrame(opponent5Edit, opponent5Pos, 3)
                    return true
                }
                "Frame 4" -> {
                    currentFrame = 4
                    setPlayersPosPerFrame(player1Edit, player1Pos, 4)
                    setPlayersPosPerFrame(player2Edit, player2Pos, 4)
                    setPlayersPosPerFrame(player3Edit, player3Pos, 4)
                    setPlayersPosPerFrame(player4Edit, player4Pos, 4)
                    setPlayersPosPerFrame(player5Edit, player5Pos, 4)
                    setPlayersPosPerFrame(ballEdit, ballPos, 4)
                    setPlayersPosPerFrame(opponent1Edit, opponent1Pos, 4)
                    setPlayersPosPerFrame(opponent2Edit, opponent2Pos, 4)
                    setPlayersPosPerFrame(opponent3Edit, opponent3Pos, 4)
                    setPlayersPosPerFrame(opponent4Edit, opponent4Pos, 4)
                    setPlayersPosPerFrame(opponent5Edit, opponent5Pos, 4)
                    return true
                }
                "Frame 5" -> {
                    currentFrame = 5
                    setPlayersPosPerFrame(player1Edit, player1Pos, 5)
                    setPlayersPosPerFrame(player2Edit, player2Pos, 5)
                    setPlayersPosPerFrame(player3Edit, player3Pos, 5)
                    setPlayersPosPerFrame(player4Edit, player4Pos, 5)
                    setPlayersPosPerFrame(player5Edit, player5Pos, 5)
                    setPlayersPosPerFrame(ballEdit, ballPos, 5)
                    setPlayersPosPerFrame(opponent1Edit, opponent1Pos, 5)
                    setPlayersPosPerFrame(opponent2Edit, opponent2Pos, 5)
                    setPlayersPosPerFrame(opponent3Edit, opponent3Pos, 5)
                    setPlayersPosPerFrame(opponent4Edit, opponent4Pos, 5)
                    setPlayersPosPerFrame(opponent5Edit, opponent5Pos, 5)
                    return true
                }
                "Frame 6" -> {
                    currentFrame = 6
                    setPlayersPosPerFrame(player1Edit, player1Pos, 6)
                    setPlayersPosPerFrame(player2Edit, player2Pos, 6)
                    setPlayersPosPerFrame(player3Edit, player3Pos, 6)
                    setPlayersPosPerFrame(player4Edit, player4Pos, 6)
                    setPlayersPosPerFrame(player5Edit, player5Pos, 6)
                    setPlayersPosPerFrame(ballEdit, ballPos, 6)
                    setPlayersPosPerFrame(opponent1Edit, opponent1Pos, 6)
                    setPlayersPosPerFrame(opponent2Edit, opponent2Pos, 6)
                    setPlayersPosPerFrame(opponent3Edit, opponent3Pos, 6)
                    setPlayersPosPerFrame(opponent4Edit, opponent4Pos, 6)
                    setPlayersPosPerFrame(opponent5Edit, opponent5Pos, 6)
                    return true
                }
                "Frame 6" -> {
                    currentFrame = 7
                    setPlayersPosPerFrame(player1Edit, player1Pos, 7)
                    setPlayersPosPerFrame(player2Edit, player2Pos, 7)
                    setPlayersPosPerFrame(player3Edit, player3Pos, 7)
                    setPlayersPosPerFrame(player4Edit, player4Pos, 7)
                    setPlayersPosPerFrame(player5Edit, player5Pos, 7)
                    setPlayersPosPerFrame(ballEdit, ballPos, 7)
                    setPlayersPosPerFrame(opponent1Edit, opponent1Pos, 7)
                    setPlayersPosPerFrame(opponent2Edit, opponent2Pos, 7)
                    setPlayersPosPerFrame(opponent3Edit, opponent3Pos, 7)
                    setPlayersPosPerFrame(opponent4Edit, opponent4Pos, 7)
                    setPlayersPosPerFrame(opponent5Edit, opponent5Pos, 7)
                    return true
                }
                "Frame 6" -> {
                    currentFrame = 8
                    setPlayersPosPerFrame(player1Edit, player1Pos, 8)
                    setPlayersPosPerFrame(player2Edit, player2Pos, 8)
                    setPlayersPosPerFrame(player3Edit, player3Pos, 8)
                    setPlayersPosPerFrame(player4Edit, player4Pos, 8)
                    setPlayersPosPerFrame(player5Edit, player5Pos, 8)
                    setPlayersPosPerFrame(ballEdit, ballPos, 8)
                    setPlayersPosPerFrame(opponent1Edit, opponent1Pos, 8)
                    setPlayersPosPerFrame(opponent2Edit, opponent2Pos, 8)
                    setPlayersPosPerFrame(opponent3Edit, opponent3Pos, 8)
                    setPlayersPosPerFrame(opponent4Edit, opponent4Pos, 8)
                    setPlayersPosPerFrame(opponent5Edit, opponent5Pos, 8)
                    return true
                }
                "Frame 6" -> {
                    currentFrame = 9
                    setPlayersPosPerFrame(player1Edit, player1Pos, 9)
                    setPlayersPosPerFrame(player2Edit, player2Pos, 9)
                    setPlayersPosPerFrame(player3Edit, player3Pos, 9)
                    setPlayersPosPerFrame(player4Edit, player4Pos, 9)
                    setPlayersPosPerFrame(player5Edit, player5Pos, 9)
                    setPlayersPosPerFrame(ballEdit, ballPos, 9)
                    setPlayersPosPerFrame(opponent1Edit, opponent1Pos, 9)
                    setPlayersPosPerFrame(opponent2Edit, opponent2Pos, 9)
                    setPlayersPosPerFrame(opponent3Edit, opponent3Pos, 9)
                    setPlayersPosPerFrame(opponent4Edit, opponent4Pos, 9)
                    setPlayersPosPerFrame(opponent5Edit, opponent5Pos, 9)
                    return true
                }
                "Frame 10" -> {
                    currentFrame = 10
                    setPlayersPosPerFrame(player1Edit, player1Pos, 10)
                    setPlayersPosPerFrame(player2Edit, player2Pos, 10)
                    setPlayersPosPerFrame(player3Edit, player3Pos, 10)
                    setPlayersPosPerFrame(player4Edit, player4Pos, 10)
                    setPlayersPosPerFrame(player5Edit, player5Pos, 10)
                    setPlayersPosPerFrame(ballEdit, ballPos, 10)
                    setPlayersPosPerFrame(opponent1Edit, opponent1Pos, 10)
                    setPlayersPosPerFrame(opponent2Edit, opponent2Pos, 10)
                    setPlayersPosPerFrame(opponent3Edit, opponent3Pos, 10)
                    setPlayersPosPerFrame(opponent4Edit, opponent4Pos, 10)
                    setPlayersPosPerFrame(opponent5Edit, opponent5Pos, 10)
                    return true
                }
                else -> return super.onOptionsItemSelected(item)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Frame has been deleted!", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        fStore.collection("tactics").get().addOnSuccessListener { result ->
            for (document in result) {
                if (document.id == tacticName) {
                    val array = document.data.get("player1") as ArrayList<HashMap<String, Float>>
                    for (i in 0 until array.size) {
                        menu?.add(Menu.NONE, frame1, Menu.NONE, "Frame $i")
                    }
                }
            }
        }
        return true
    }


    /*
  * this method clears all of the playerPos arrays
  * used when going back to previous activity to ensure that they are re-setted each time the player loads create tactic activity
   */
    fun clearPlayerPosArrays() {
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
    * this method will get the position of each player in each frame from the firestore for the specific tactic
     */
    private fun getTacticData() {
        fStore.collection("tactics").get().addOnSuccessListener { result ->
            for (document in result) {
                if (document.id == tacticName) {
                    fStore.collection("tactics").document(tacticName).get()
                        .addOnSuccessListener {
                            for (frame in document.data) {
                                if (frame.key == "player1") {
                                    var player1Map =
                                        frame.value as ArrayList<HashMap<String, Float>>
                                    for (position in player1Map) {
                                        if (diffScreenSize) {
                                            var creatorScreenHeight: Float = getRatio().first
                                            var creatorScreenWidth: Float = getRatio().second
                                            var newX: Float =
                                                position.get("first")!! * (creatorScreenWidth)
                                            var newY: Float =
                                                position.get("second")!! * (creatorScreenHeight)
                                            player1Pos.add(Pair(newX, newY))
                                        } else {
                                            player1Pos.add(
                                                Pair(
                                                    position.get("first"),
                                                    position.get("second")
                                                ) as Pair<Float, Float>
                                            )
                                        }
                                    }

                                } else if (frame.key == "player2") {
                                    var player2Map =
                                        frame.value as ArrayList<HashMap<String, Float>>
                                    for (position in player2Map) {
                                        if (diffScreenSize) {
                                            var creatorScreenHeight: Float = getRatio().first
                                            var creatorScreenWidth: Float = getRatio().second
                                            var newX: Float =
                                                position.get("first")!! * (creatorScreenWidth)
                                            var newY: Float =
                                                position.get("second")!! * (creatorScreenHeight)
                                            player2Pos.add(Pair(newX, newY))
                                            println("THIS IS THE PAIR $newX $newY")
                                        } else {
                                            player2Pos.add(
                                                Pair(
                                                    position.get("first"),
                                                    position.get("second")
                                                ) as Pair<Float, Float>
                                            )
                                        }
                                    }
                                } else if (frame.key == "player3") {
                                    var player3Map =
                                        frame.value as ArrayList<HashMap<String, Float>>
                                    for (position in player3Map) {
                                        if (diffScreenSize) {
                                            var creatorScreenHeight: Float = getRatio().first
                                            var creatorScreenWidth: Float = getRatio().second
                                            var newX: Float =
                                                position.get("first")!! * (creatorScreenWidth)
                                            var newY: Float =
                                                position.get("second")!! * (creatorScreenHeight)
                                            player3Pos.add(Pair(newX, newY))
                                            println("THIS IS THE PAIR $newX $newY")
                                        } else {
                                            player3Pos.add(
                                                Pair(
                                                    position.get("first"),
                                                    position.get("second")
                                                ) as Pair<Float, Float>
                                            )
                                        }
                                    }
                                } else if (frame.key == "player4") {
                                    var player4Map =
                                        frame.value as ArrayList<HashMap<String, Float>>
                                    for (position in player4Map) {
                                        if (diffScreenSize) {
                                            var creatorScreenHeight: Float = getRatio().first
                                            var creatorScreenWidth: Float = getRatio().second
                                            var newX: Float =
                                                position.get("first")!! * (creatorScreenWidth)
                                            var newY: Float =
                                                position.get("second")!! * (creatorScreenHeight)
                                            player4Pos.add(Pair(newX, newY))
                                            println("THIS IS THE PAIR $newX $newY")
                                        } else {
                                            player4Pos.add(
                                                Pair(
                                                    position.get("first"),
                                                    position.get("second")
                                                ) as Pair<Float, Float>
                                            )
                                        }
                                    }
                                } else if (frame.key == "player5") {
                                    var player5Map =
                                        frame.value as ArrayList<HashMap<String, Float>>
                                    for (position in player5Map) {
                                        if (diffScreenSize) {
                                            var creatorScreenHeight: Float = getRatio().first
                                            var creatorScreenWidth: Float = getRatio().second
                                            var newX: Float =
                                                position.get("first")!! * (creatorScreenWidth)
                                            var newY: Float =
                                                position.get("second")!! * (creatorScreenHeight)
                                            player5Pos.add(Pair(newX, newY))
                                            println("THIS IS THE PAIR $newX $newY")
                                        } else {
                                            player5Pos.add(
                                                Pair(
                                                    position.get("first"),
                                                    position.get("second")
                                                ) as Pair<Float, Float>
                                            )
                                        }
                                    }
                                } else if (frame.key == "ball") {
                                    var ballMap = frame.value as ArrayList<HashMap<String, Float>>
                                    for (position in ballMap) {
                                        if (diffScreenSize) {
                                            var creatorScreenHeight: Float = getRatio().first
                                            var creatorScreenWidth: Float = getRatio().second
                                            var newX: Float =
                                                position.get("first")!! * (creatorScreenWidth)
                                            var newY: Float =
                                                position.get("second")!! * (creatorScreenHeight)
                                            ballPos.add(Pair(newX, newY))
                                            println("THIS IS THE PAIR $newX $newY")
                                        } else {
                                            ballPos.add(
                                                Pair(
                                                    position.get("first"),
                                                    position.get("second")
                                                ) as Pair<Float, Float>
                                            )
                                        }
                                    }
                                } else if (frame.key == "opponent1") {
                                    var opponent1Map =
                                        frame.value as ArrayList<HashMap<String, Float>>
                                    for (position in opponent1Map) {
                                        if (diffScreenSize) {
                                            var creatorScreenHeight: Float = getRatio().first
                                            var creatorScreenWidth: Float = getRatio().second
                                            var newX: Float =
                                                position.get("first")!! * (creatorScreenWidth)
                                            var newY: Float =
                                                position.get("second")!! * (creatorScreenHeight)
                                            opponent1Pos.add(Pair(newX, newY))
                                            println("THIS IS THE PAIR $newX $newY")
                                        } else {
                                            opponent1Pos.add(
                                                Pair(
                                                    position.get("first"),
                                                    position.get("second")
                                                ) as Pair<Float, Float>
                                            )
                                        }
                                    }
                                } else if (frame.key == "opponent2") {
                                    var opponent2Map =
                                        frame.value as ArrayList<HashMap<String, Float>>
                                    for (position in opponent2Map) {
                                        if (diffScreenSize) {
                                            var creatorScreenHeight: Float = getRatio().first
                                            var creatorScreenWidth: Float = getRatio().second
                                            var newX: Float =
                                                position.get("first")!! * (creatorScreenWidth)
                                            var newY: Float =
                                                position.get("second")!! * (creatorScreenHeight)
                                            opponent2Pos.add(Pair(newX, newY))
                                            println("THIS IS THE PAIR $newX $newY")
                                        } else {
                                            opponent2Pos.add(
                                                Pair(
                                                    position.get("first"),
                                                    position.get("second")
                                                ) as Pair<Float, Float>
                                            )
                                        }
                                    }
                                } else if (frame.key == "opponent3") {
                                    var opponent3Map =
                                        frame.value as ArrayList<HashMap<String, Float>>
                                    for (position in opponent3Map) {
                                        if (diffScreenSize) {
                                            var creatorScreenHeight: Float = getRatio().first
                                            var creatorScreenWidth: Float = getRatio().second
                                            var newX: Float =
                                                position.get("first")!! * (creatorScreenWidth)
                                            var newY: Float =
                                                position.get("second")!! * (creatorScreenHeight)
                                            opponent3Pos.add(Pair(newX, newY))
                                            println("THIS IS THE PAIR $newX $newY")
                                        } else {
                                            opponent3Pos.add(
                                                Pair(
                                                    position.get("first"),
                                                    position.get("second")
                                                ) as Pair<Float, Float>
                                            )
                                        }
                                    }
                                } else if (frame.key == "opponent4") {
                                    var opponent4Map =
                                        frame.value as ArrayList<HashMap<String, Float>>
                                    for (position in opponent4Map) {
                                        if (diffScreenSize) {
                                            var creatorScreenHeight: Float = getRatio().first
                                            var creatorScreenWidth: Float = getRatio().second
                                            var newX: Float =
                                                position.get("first")!! * (creatorScreenWidth)
                                            var newY: Float =
                                                position.get("second")!! * (creatorScreenHeight)
                                            opponent4Pos.add(Pair(newX, newY))
                                            println("THIS IS THE PAIR $newX $newY")
                                        } else {
                                            opponent4Pos.add(
                                                Pair(
                                                    position.get("first"),
                                                    position.get("second")
                                                ) as Pair<Float, Float>
                                            )
                                        }
                                    }
                                } else if (frame.key == "opponent5") {
                                    var opponent5Map =
                                        frame.value as ArrayList<HashMap<String, Float>>
                                    for (position in opponent5Map) {
                                        if (diffScreenSize) {
                                            var creatorScreenHeight: Float = getRatio().first
                                            var creatorScreenWidth: Float = getRatio().second
                                            var newX: Float =
                                                position.get("first")!! * (creatorScreenWidth)
                                            var newY: Float =
                                                position.get("second")!! * (creatorScreenHeight)
                                            opponent5Pos.add(Pair(newX, newY))
                                            println("THIS IS THE PAIR $newX $newY")
                                        } else {
                                            opponent5Pos.add(
                                                Pair(
                                                    position.get("first"),
                                                    position.get("second")
                                                ) as Pair<Float, Float>
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        .addOnFailureListener { e -> Log.w(TAG, "No such document", e) }
                }
            }
        }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    private fun getRatio(): Pair<Float, Float> {
        fStore.collection("tactics").get().addOnSuccessListener { result ->
            for (document in result) {
                if (document.id == tacticName) {
                    fStore.collection("tactics").document(tacticName).get()
                        .addOnSuccessListener {
                            for (frame in document.data) {
                                if (frame.key == "screenSizeCreator") {
                                    var screenSizeCreator = frame.value as HashMap<String, Float>
                                    var creatorHeight = screenSizeCreator.get("first").toString()
                                    var creatorWidth = screenSizeCreator.get("second").toString()
                                    var thisScreenHeight: Float = getScreenSize().first
                                    var thisScreenWidth: Float = getScreenSize().second
                                    if (!(creatorHeight.toFloat() == thisScreenHeight && creatorWidth.toFloat() == thisScreenWidth)) {
                                        screenRatioHeight =
                                            thisScreenHeight.div(creatorHeight.toFloat())
                                        screenRatioWidth =
                                            thisScreenWidth.div(creatorWidth.toFloat())
                                        diffScreenSize = true
                                    }
                                }
                            }

                        }
                        .addOnFailureListener { e -> Log.w(TAG, "No such document", e) }
                }
            }
        }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
        return Pair(screenRatioHeight, screenRatioWidth)
    }

    private fun getScreenSize(): Pair<Float, Float> {
        val displayMetrics = DisplayMetrics()
        val windowsManager = applicationContext.getSystemService(WINDOW_SERVICE) as WindowManager
        windowsManager.defaultDisplay.getMetrics(displayMetrics)
        val deviceHeight = displayMetrics.heightPixels.toFloat()
        val deviceWidth = displayMetrics.widthPixels.toFloat()
        return Pair(deviceHeight, deviceWidth)
    }

    private val dragListener = View.OnDragListener { view, event ->
        when (event.action) {
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
            DragEvent.ACTION_DRAG_ENDED -> {
                view.invalidate()

                // Does a getResult(), and displays what happened.
                when (event.result) {
                    true ->
                        Toast.makeText(this, "Player moved.", Toast.LENGTH_SHORT)
                    else -> {
                        val v = event.localState as View
                        v.visibility = View.VISIBLE
                        Toast.makeText(
                            this,
                            "Keep the player within the pitch!",
                            Toast.LENGTH_SHORT
                        )
                    }

                }.show()

                true
            }
            else -> false
        }
    }

}