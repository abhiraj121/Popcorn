package com.abhirajsharma.popcorn.Db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.*
import com.abhirajsharma.popcorn.movies_gen.Movies

class FavoriteDbHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    var dbhandler: SQLiteOpenHelper? = null
    var db: SQLiteDatabase? = null
    fun open() {
        Log.i(LOGTAG, "Database Opened")
        db = dbhandler!!.writableDatabase
    }

    override fun close() {
        Log.i(LOGTAG, "Database Closed")
        dbhandler!!.close()
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val SQL_CREATE_FAVORITE_TABLE =
            "CREATE TABLE " + FavoriteContract.FavoriteEntry.TABLE_NAME + " (" +
                    FavoriteContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FavoriteContract.FavoriteEntry.COLUMN_MOVIEID + " INTEGER, " +
                    FavoriteContract.FavoriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                    FavoriteContract.FavoriteEntry.COLUMN_USERRATING + " REAL NOT NULL, " +
                    FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                    FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS + " TEXT NOT NULL" +
                    "); "
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.FavoriteEntry.TABLE_NAME)
        onCreate(sqLiteDatabase)
    }

    fun addFavorite(movie: Movies) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIEID, movie.id)
        values.put(FavoriteContract.FavoriteEntry.COLUMN_TITLE, movie.originalTitle)
        values.put(FavoriteContract.FavoriteEntry.COLUMN_USERRATING, movie.voteAverage)
        values.put(FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH, movie.posterPath)
        values.put(FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS, movie.overview)
        db.insert(FavoriteContract.FavoriteEntry.TABLE_NAME, null, values)
        db.close()
    }

    fun deleteFavorite(id: Int) {
        val db = this.writableDatabase
        db.delete(
            FavoriteContract.FavoriteEntry.TABLE_NAME,
            FavoriteContract.FavoriteEntry.COLUMN_MOVIEID.toString() + "=" + id,
            null
        )
    }

    val allFavorite: List<Movies>
        get() {
            val columns = arrayOf<String>(
                FavoriteContract.FavoriteEntry._ID,
                FavoriteContract.FavoriteEntry.COLUMN_MOVIEID,
                FavoriteContract.FavoriteEntry.COLUMN_TITLE,
                FavoriteContract.FavoriteEntry.COLUMN_USERRATING,
                FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH,
                FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS
            )
            val sortOrder: String = FavoriteContract.FavoriteEntry._ID.toString() + " ASC"
            val favoriteList: MutableList<Movies> = ArrayList()
            val db = this.readableDatabase
            val cursor = db.query(
                FavoriteContract.FavoriteEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                sortOrder
            )
            if (cursor.moveToFirst()) {
                do {
                    val movie = Movies()
                    movie.id = cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIEID)).toInt()
                    movie.originalTitle = cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_TITLE))
                    movie.voteAverage = cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_USERRATING)).toDouble()
                    movie.posterPath = cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH))
                    movie.overview = cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS))
                    favoriteList.add(movie)
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return favoriteList
        }

    companion object {
        private const val DATABASE_NAME = "favorite.db"
        private const val DATABASE_VERSION = 1
        const val LOGTAG = "FAVORITE"
    }
}
