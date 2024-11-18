package com.android.platform.ui.exercises.placement.adapter

class PlacementEntity (
    val Title: String,
    var Word: String,
    var selected:ArrayList<Pair<Int,String>>
)
fun PlacementEntity.getWords():ArrayList<String>{
    return this.Word.split("+") as ArrayList
}
fun PlacementEntity.addSelected(value: String,position:Int)=selected.add(Pair<Int,String>(position,value))
fun PlacementEntity.removeSelected(value: String)=selected.removeIf{it.second==value}
fun PlacementEntity.getSelected(position:Int):ArrayList<String>{
    val data = ArrayList<String>()
    if (selected==null)
        selected = arrayListOf()
    selected.filter { it.first==position }.forEach{
        data.add(it.second)
    }
    return data
}
