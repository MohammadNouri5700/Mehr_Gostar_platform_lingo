package com.android.platform.ui.exercises.placement.adapter

class PlacementEntity (
    val Title: String,
    val Word: String,
    val itemsSelected:ArrayList<String>
)
fun PlacementEntity.getWords():ArrayList<String>{
    return this.Word.split("+") as ArrayList
}
fun PlacementEntity.getSelected():ArrayList<String>{
    if (itemsSelected==null){
        return arrayListOf()
    }else{
        return itemsSelected
    }
}