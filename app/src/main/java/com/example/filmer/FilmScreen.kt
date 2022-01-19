package com.example.filmer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class FilmScreen : AppCompatActivity() {
    val FILMS_ON_PAGE = 20
    val MAX_COUNT_FILMS = 10000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_screen)
        val titleTextView: TextView = findViewById(R.id.title)
        val releaseTextView: TextView = findViewById(R.id.release)
        val genresTextView: TextView = findViewById(R.id.genres)
        val rateTextView: TextView = findViewById(R.id.rate)
        val overviewTextView: TextView = findViewById(R.id.overview)
        val posterImageView: ImageView = findViewById(R.id.imageView)
        val discoverUrl = intent.extras?.getString("discoverUrl")
        val totalResults = intent.extras?.getInt("totalResults")
        val totalPages = intent.extras?.getInt("totalPages")
        if (discoverUrl != null && totalResults != null && totalPages != null) {
            val selectedFilm = if (totalResults <= MAX_COUNT_FILMS){
                (0 until totalResults).random()
            }else{
                (0 until MAX_COUNT_FILMS).random()
            }

            val indexOnPage = selectedFilm%FILMS_ON_PAGE
            val selectedPage = (selectedFilm/FILMS_ON_PAGE) + 1
            println(discoverUrl.dropLast(1) + selectedPage)
            doAsync {
                val apiResponse = URL(discoverUrl.dropLast(1) + selectedPage).readText()
                val filmJson = JSONObject(apiResponse).getJSONArray("results").getJSONObject(indexOnPage)
                val filmName = filmJson.getString("title")
                val rate = filmJson.getDouble("vote_average")
                val release = filmJson.getString("release_date")
                val overview = filmJson.getString("overview")
                val posterUrl = "https://image.tmdb.org/t/p/original" + filmJson.getString("poster_path")
                val genres = getGenres(filmJson.getJSONArray("genre_ids"))
                uiThread {
                    titleTextView.text = filmName
                    rateTextView.text = rate.toString()
                    releaseTextView.text = release
                    genresTextView.text = genres
                    overviewTextView.text = overview
                    overviewTextView.movementMethod = ScrollingMovementMethod() // make overview scroll
                    println(filmJson.toString())
                    Picasso.get().load(posterUrl).into(posterImageView)
                }
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
}