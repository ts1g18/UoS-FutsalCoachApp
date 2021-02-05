package com.example.uosfutsalcoachapp

import android.widget.Button

//framesList stores the position (x,y) of each player(value) and links it with the current frame
var framesList = HashMap<Int,HashMap<Button,Pair<Float,Float>>>()
class Tactic {
    //this method returns the position of all players in the frame provided as parameter
   fun getTacticFrame(frame:Int): HashMap<Button, Pair<Float, Float>>? {
       return framesList.get(frame)
   }
    fun getTactic(): HashMap<Int,HashMap<Button,Pair<Float,Float>>>{
        return framesList
    }
    fun setTactic(frameNumber:Int, frame:HashMap<Button,Pair<Float,Float>>){
        framesList.put(frameNumber,frame)
    }
}