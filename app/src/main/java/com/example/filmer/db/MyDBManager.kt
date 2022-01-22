package com.example.filmer.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.filmer.Entity.FilmEntity

class MyDBManager(context: Context) {
    val myDBHelper = MyDBHelper(context)
    var db: SQLiteDatabase? = null

    fun openDB(){
        db = myDBHelper.writableDatabase
    }

    fun insertToDB(title: String, content: String, posterPath: String, genres: String, language: String){
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE, title)
            put(MyDbNameClass.COLUMN_NAME_CONTENT, content)
            put(MyDbNameClass.COLUMN_POSTER_PATH, posterPath)
            put(MyDbNameClass.COLUMN_GENRES, genres)
            put(MyDbNameClass.COLUMN_LANGUAGE, language)
        }
        db?.insert(MyDbNameClass.TABLE_NAME, null, values)
    }
    fun readFromDB() : ArrayList<FilmEntity>{
        val dataList = ArrayList<FilmEntity>()
        val cursor = db?.query(MyDbNameClass.TABLE_NAME, null,null,null,null,null,null)
        if(cursor!!.moveToFirst()){
            do{
                val filmEntity = FilmEntity(
                    cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_CONTENT)),
                    cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_GENRES)),
                    cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_LANGUAGE))
                    )
                filmEntity.id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
                filmEntity.posterPath = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_POSTER_PATH))
                dataList.add(filmEntity)
            }while (cursor.moveToNext())
        }
        cursor.close()
        return dataList
    }
    fun deleteFromDB(id : Int) : Boolean {
        return db!!.delete(MyDbNameClass.TABLE_NAME, "${BaseColumns._ID} = $id", null) > 0
    }
}