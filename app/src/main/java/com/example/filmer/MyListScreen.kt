package com.example.filmer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmer.db.MyDBManager
import com.google.android.material.snackbar.Snackbar

class MyListScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_list_screen)
        val recyclerView: RecyclerView = findViewById(R.id.list_of_films)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dbManager = MyDBManager(this)
        dbManager.openDB()
        val allFilms = dbManager.readFromDB()
        val adapter = MyAdapter(allFilms)
        recyclerView.adapter = adapter
        
        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedCourse = allFilms[viewHolder.adapterPosition]
                val position = viewHolder.adapterPosition
                allFilms.removeAt(viewHolder.adapterPosition)
                if (!dbManager.deleteFromDB(deletedCourse.id)) {
                    Toast.makeText(
                        viewHolder.itemView.context,
                        "Not found item with id - " + deletedCourse.id,
                        Toast.LENGTH_LONG
                    ).show()
                }
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                Snackbar.make(
                    recyclerView,
                    deletedCourse.filmName + " removed",
                    Snackbar.LENGTH_LONG
                ).setAction(
                    resources.getString(R.string.Undo)
                ) {
                    allFilms.add(position, deletedCourse)
                    dbManager.insertToDB(deletedCourse.filmName, deletedCourse.content, deletedCourse.posterPath, deletedCourse.genres, deletedCourse.language)
                    adapter.notifyItemInserted(position)
                }.show()
            }
        }).attachToRecyclerView(recyclerView)

    }
}