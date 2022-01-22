package com.example.filmer

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import com.example.filmer.Entity.FilmEntity
import com.example.filmer.db.MyDBManager
import com.squareup.picasso.Picasso
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.URL

class FilmScreen : AppCompatActivity() {
    val FILMS_ON_PAGE = 20
    val MAX_COUNT_FILMS = 10000
    var currentFilm: FilmEntity? = null
    var titleTextView: TextView? = null
    var releaseTextView: TextView? = null
    var genresTextView: TextView? = null
    var rateTextView: TextView? = null
    var overviewTextView: TextView? = null
    var posterImageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_screen)
        val myDBManager = MyDBManager(this)
        myDBManager.openDB()

        titleTextView = findViewById(R.id.title)
        releaseTextView = findViewById(R.id.release)
        genresTextView = findViewById(R.id.genres)
        rateTextView = findViewById(R.id.rate)
        overviewTextView = findViewById(R.id.overview)
        overviewTextView?.movementMethod = ScrollingMovementMethod() // make overview scroll
        posterImageView = findViewById(R.id.imageView)
        val likeButton: Button = findViewById(R.id.like)
        val skipButton: Button = findViewById(R.id.skip)
        val discoverUrl = intent.extras?.getString("discoverUrl")
        val totalResults = intent.extras?.getInt("totalResults")
        if (discoverUrl != null && totalResults != null) {
            updateFilm(discoverUrl, totalResults)
        }else{
            Toast.makeText(this, "We didn't find any films", Toast.LENGTH_SHORT).show()
            intent = Intent(this, SearchScreen::class.java)
            startActivity(intent)
        }

        likeButton.setOnClickListener {
            val bitmap = posterImageView?.drawable?.toBitmap()
            val contextWrapper = ContextWrapper(it.context)
            val directory = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE)
            val filename = System.currentTimeMillis().toString() + ".jpg"
            val myPath = File(directory,filename)
            var fos : FileOutputStream? = null
            try {
                fos = FileOutputStream(myPath)
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 20, fos)
                myDBManager.insertToDB(currentFilm!!.filmName, currentFilm!!.content,
                    directory.absolutePath.toString() + "/" + filename,
                    genresTextView?.text.toString(),
                    currentFilm!!.language)
                updateFilm(discoverUrl!!, totalResults!!)
            }catch (e : Exception){
                println("Can't save bitmap because of next error: ")
                e.printStackTrace()
            } finally {
                if (fos != null){
                    try {
                        fos.close()
                    } catch (e : Exception){
                        println("fos can't be close because of next error: ")
                        e.printStackTrace()
                    }
                }
            }
        }
        skipButton.setOnClickListener {
            updateFilm(discoverUrl!!, totalResults!!)
        }

    }

    private fun updateFilm(discoverUrl : String, totalResults : Int){
        val selectedFilm = if (totalResults <= MAX_COUNT_FILMS){
            (0 until totalResults).random()
        }else{
            (0 until MAX_COUNT_FILMS).random()
        }

        val indexOnPage = selectedFilm%FILMS_ON_PAGE
        val selectedPage = (selectedFilm/FILMS_ON_PAGE) + 1

        doAsync {
            val apiResponse = URL(discoverUrl.dropLast(1) + selectedPage).readText()
            val filmJson = JSONObject(apiResponse).getJSONArray("results").getJSONObject(indexOnPage)
            val languageId = filmJson.getString("original_language").toString()
            val filmName = filmJson.getString("title")
            currentFilm = FilmEntity(filmJson.toString(),
                getGenres(filmJson.getJSONArray("genre_ids")),
                getLanguages(languageId))
            val posterUrl = "https://image.tmdb.org/t/p/original" + filmJson.getString("poster_path")
            onComplete {
                titleTextView?.text = filmName.toString()
                rateTextView?.text = currentFilm!!.rate.toString()
                releaseTextView?.text = currentFilm!!.release
                genresTextView?.text = currentFilm!!.genres
                overviewTextView?.text = currentFilm!!.overview
                Picasso.get().load(posterUrl).into(posterImageView)
            }
        }
    }

    private fun getGenres(genresJson: JSONArray): String {
        var result = ""
        for (n in 0 until genresJson.length()){
            result += mapGenres?.get(genresJson.getInt(n)) + ","
        }
        return result.dropLast(1)
    }
    private fun getLanguages(languageId: String): String {
        return mapLanguages!![languageId]!!
    }
}