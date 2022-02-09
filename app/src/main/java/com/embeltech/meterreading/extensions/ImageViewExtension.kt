
package com.embeltech.meterreading.extensions

import android.widget.ImageView

fun ImageView.loadImage(imageUrl: String) {
    //Picasso.with(context).load(File(imageUrl)).centerCrop().resize(400, 400).placeholder(R.drawable.ic_person_outline_black_24dp).into(this)
    /*Glide
            .with(context)
            .load(File(imageUrl))
            .centerCrop()
            .placeholder(R.drawable.ic_person_outline_black_24dp)
            .into(this)*/


}
fun ImageView.loadImage(resourceId: Int) {
    //Picasso.with(context).load(resourceId).into(this)
    this.setImageResource(resourceId)
}