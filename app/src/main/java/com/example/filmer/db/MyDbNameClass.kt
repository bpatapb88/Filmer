package com.example.filmer.db

import android.provider.BaseColumns

object MyDbNameClass : BaseColumns {
    const val TABLE_NAME = "my_table"
    const val COLUMN_NAME_TITLE = "film_name"
    const val COLUMN_NAME_CONTENT = "value"
    const val COLUMN_POSTER_PATH = "path"
    const val COLUMN_GENRES = "genres"
    const val COLUMN_LANGUAGE = "language"

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "TheMovieDB.db"

    const val SQL_CREATE_ENTRIES =
        "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "$COLUMN_NAME_TITLE TEXT," +
                "$COLUMN_NAME_CONTENT TEXT UNIQUE," +
                "$COLUMN_POSTER_PATH TEXT," +
                "$COLUMN_GENRES TEXT," +
                "$COLUMN_LANGUAGE TEXT)"

    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}