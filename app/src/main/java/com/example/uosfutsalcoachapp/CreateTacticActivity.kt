package com.example.uosfutsalcoachapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

class CreateTacticActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_tactic)

        //call the action bar
        val actionBar = supportActionBar

        //show the back button in action bar
        if(actionBar != null){
            //set action bar title
                actionBar!!.title = "Home Screen Activiy"
            //set back button
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
    }


    //when back is pressed go to previous activity
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}