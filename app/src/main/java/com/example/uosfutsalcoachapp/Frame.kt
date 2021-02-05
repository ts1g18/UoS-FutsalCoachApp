package com.example.uosfutsalcoachapp

import android.widget.Button

//playerPositions is hashmap that stores the position of all player pins (button)
val playerPositions = HashMap<Button,Pair<Float,Float>>()
/*
* this class represents a frame
* a frame simply is one specific movement in the tactic
* in each frame the playerpositions must be stored
* then by playing the frames in sequence we get a tactic (which is made up of many frames depending on how many parts the tactic has)
 */
class Frame {
    fun getPlayerXPosition(player:Button): Float? {
        return playerPositions.get(player)?.first
    }
    fun getPlayerYPosition(player:Button): Float? {
        return playerPositions.get(player)?.second
    }
    fun setFrame(player:Button, position:Pair<Float,Float>){
        playerPositions.put(player,position)
    }
    fun getFrame():HashMap<Button,Pair<Float,Float>>{
        return playerPositions
    }
}