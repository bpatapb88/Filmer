package com.example.filmer.Entity

import com.example.filmer.FilmScreen
import org.json.JSONObject

class FilmEntity(_content: String, genres: String, language: String) {
    var content: String = _content
    var filmName: String = JSONObject(content).getString("title")
    var id: Int = -1
    var posterPath: String = ""
    var release: String = JSONObject(content).getString("release_date")
    var rate: Double = JSONObject(content).getDouble("vote_average")
    var genres: String = genres
    var language: String = language
    var overview: String = JSONObject(content).getString("overview")

}
