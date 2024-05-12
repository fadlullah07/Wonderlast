package com.pmm.wonderlast

data class GalleryItem(
    val id: String,
    val imageUrl: String,
    val title: String,
    val location: String,
    var isMarked : Boolean,
    val rating : String,
){
    fun toggle() {
        isMarked = !isMarked
    }
}

