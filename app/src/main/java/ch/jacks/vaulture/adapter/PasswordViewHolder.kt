package ch.jacks.vaulture.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.jacks.vaulture.R

class PasswordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val passwordCardImage: ImageView = view.findViewById(R.id.passwordCardImage)
    val passwordCardName: TextView = view.findViewById(R.id.passwordCardName)
    val passwordCardUrl: TextView = view.findViewById(R.id.passwordCardUrl)
}