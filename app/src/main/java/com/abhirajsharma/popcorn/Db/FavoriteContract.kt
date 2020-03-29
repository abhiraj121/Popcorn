package com.abhirajsharma.popcorn.Db

class FavoriteContract {
    object FavoriteEntry  {
        const val _ID = "rowid"
        const val _COUNT = "_count"
        const val TABLE_NAME = "favorite"
        const val COLUMN_MOVIEID = "movieid"
        const val COLUMN_TITLE = "title"
        const val COLUMN_USERRATING = "userrating"
        const val COLUMN_POSTER_PATH = "posterpath"
        const val COLUMN_PLOT_SYNOPSIS = "overview"
    }
}
