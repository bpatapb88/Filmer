package com.example.filmer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {
    private var search_btn: Button? = null
    private var search_field: EditText? = null
    private var imageView: ImageView? = null
    private var film_name: TextView? = null
    private var searchButton: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        search_field = findViewById(R.id.searchField)
        search_btn = findViewById(R.id.button)
        searchButton = findViewById(R.id.search)
        imageView = findViewById(R.id.imageView)
        film_name = findViewById(R.id.textView)

        search_btn?.setOnClickListener {
            println(BuildConfig.API_KEY)

            if(search_field?.text?.toString()?.trim()?.equals("")!!)
                Toast.makeText(this, "Search fieald is empty!", Toast.LENGTH_LONG).show()
            else{
                var request: String = search_field?.text.toString()
                var key: String = "125a923057a246d561b25dac6396d17b"
                var url: String = "https://api.themoviedb.org/3/search/movie?" +
                        "api_key=$key&" +
                        "language=en&" +
                        "query=$request&" +
                        "page=1&" +
                        "include_adult=false"
                var poster_url: String = "https://image.tmdb.org/t/p/original"
                doAsync {
                    val apiResponse = URL(url).readText()
                    val results = JSONObject(apiResponse).getJSONArray("results")
                    val first = results.getJSONObject(0)
                    val title = first.getString("original_title")
                    val posterUrl = poster_url + first.getString("poster_path")
                    onComplete {
                        film_name?.text = title
                        setImage(posterUrl)
                    }
                }
            }
        }

        searchButton?.setOnClickListener {
            intent = Intent(this, SearchScreen::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {

    }

    fun setImage (url: String){
        Picasso.get().load(url).into(imageView)
    }
}