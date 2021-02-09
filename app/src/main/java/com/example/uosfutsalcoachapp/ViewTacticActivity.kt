package com.example.uosfutsalcoachapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class ViewTacticActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_tactic)
        setTitle("Tactics")
        val tacticList : ListView = findViewById(R.id.tactic_list_view)
        //TODO ARRAY HAS TO BE THE TACTICS ARRAY THAT CAN BE FETCHED FROM FIRESTORE DATABASE (COLLECTION TACTICS)
        var array = arrayOf("Melbourne", "Vienna", "Vancouver", "Toronto", "Calgary", "Adelaide", "Perth", "Auckland", "Helsinki", "Hamburg", "Munich", "New York", "Sydney", "Paris", "Cape Town", "Barcelona", "London", "Bangkok")
        val adapter = ArrayAdapter(this,
                R.layout.tactic_list_layout, array)
        tacticList.setAdapter(adapter)
    }
}