package com.example.filmer

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.filmer.Entity.FilmEntity
import com.squareup.picasso.Picasso

class MyAdapter(private val mList: List<FilmEntity>) :  RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val oneFilm = mList[position]
        // sets the image to the imageview from our itemHolder class
        val imageUri = Uri.parse(oneFilm.posterPath)
        holder.imageView.setImageURI(imageUri)
        // sets the text to the textview from our itemHolder class
        holder.textView.text = oneFilm.filmName
        holder.textGenres.text = "Generes: " + oneFilm.genres
        holder.textRelease.text = "Release: " + oneFilm.release
        holder.textRate.text = "Rating: " + oneFilm.rate.toString()
    }
    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.poster)
        val textView: TextView = itemView.findViewById(R.id.title)
        val textRelease: TextView = itemView.findViewById(R.id.release)
        val textGenres: TextView = itemView.findViewById(R.id.genres)
        val textRate: TextView = itemView.findViewById(R.id.rating)
    }
}