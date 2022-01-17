package com.example.filmer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

// genres availible - Action, Adventure, Animation, Comedy, Crime, Documentary, Drama, Family, Fantasy, History, Horror, Music, Mystery, Romance, Science Fiction, Thriller, TV Movie, War, and Western.

private var normalSpinner: Spinner? = null
private var mapGenres: MutableMap<Int, String>? = null
private var mapLanguages: MutableMap<String, String>? = null
private var languageTextView: AutoCompleteTextView? = null

class SearchScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_screen)
        normalSpinner = findViewById(R.id.genresSpinner)
        languageTextView = findViewById(R.id.autoCompleteTextView)
        var url: String = "https://api.themoviedb.org/3/genre/movie/list?api_key=${BuildConfig.API_KEY}&language=en-US"
        var urlLanguage: String = "https://api.themoviedb.org/3/configuration/languages?api_key=${BuildConfig.API_KEY}"
        val contextCurrent = this
        doAsync {
                var apiResponse = URL(url).readText()
                val allGenres = JSONObject(apiResponse).getJSONArray("genres")
                mapGenres = mutableMapOf()
                for (n in 0 until allGenres.length()) {
                    val idGenres: Int = allGenres.getJSONObject(n).getInt("id")
                    val nameGenres: String = allGenres.getJSONObject(n).getString("name")
                    mapGenres!![idGenres] = nameGenres
                    println("id $idGenres name $nameGenres")
                }

                apiResponse = URL(urlLanguage).readText()
                val allLanguages = JSONArray(apiResponse)
                mapLanguages = mutableMapOf()
                for (n in 0 until allLanguages.length()) {
                    val isoLang: String = allLanguages.getJSONObject(n).getString("iso_639_1")
                    val nameLanguage: String = allLanguages.getJSONObject(n).getString("english_name")
                    mapLanguages!![isoLang] = nameLanguage
                }
                onComplete {
                    normalSpinner!!.adapter = ArrayAdapter(normalSpinner!!.context,
                        android.R.layout.simple_spinner_item, ArrayList(mapGenres!!.values))
                    languageTextView!!.setAdapter(ArrayAdapter(
                        languageTextView!!.context,android.R.layout.simple_dropdown_item_1line, ArrayList(
                        mapLanguages!!.values)))
                }
        }




    }
}