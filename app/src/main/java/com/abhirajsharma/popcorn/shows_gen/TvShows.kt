package com.abhirajsharma.popcorn.shows_gen

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class TvShows : Parcelable {
    @SerializedName("key")
    var key: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("first_air_date")
    var firstAirDate: String? = null

    var overview: String? = null

    @SerializedName("original_language")
    var originalLanguage: String? = null

    @SerializedName("genre_ids")
    var genreIds: List<Int?>? = null

    @SerializedName("poster_path")
    var posterPath: String? = null

    @SerializedName("origin_country")
    var originCountry: List<String?>? = null

    @SerializedName("")
    var backdropPath: String? = null

    @SerializedName("original_name")
    var originalName: String? = null

    var popularity: Double? = null

    @SerializedName("vote_average")
    var voteAverage: Double? = null

    var id: Int? = null

    var voteCount: Int? = null

    constructor(
        key: String?,
        name: String?,
        firstAirDate: String?,
        overview: String?,
        originalLanguage: String?,
        genreIds: List<Int?>?,
        posterPath: String?,
        originCountry:List<String?>?,
        backdropPath: String?,
        originalName: String?,
        popularity: Double?,
        voteAverage: Double?,
        id: Int?,
        voteCount: Int?
    ){
        this.key = key
        this.name = name
        this.firstAirDate = firstAirDate
        this.overview = overview
        this.originalLanguage = originalLanguage
        this.genreIds = genreIds
        this.posterPath = posterPath
        this.originCountry = originCountry
        this.backdropPath = backdropPath
        this.originalName = originalName
        this.popularity = popularity
        this.voteAverage = voteAverage
        this.id = id
        this.voteCount = voteCount
    }

    constructor() {}

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(key)
        parcel.writeString(name)
        parcel.writeString(firstAirDate)
        parcel.writeString(overview)
        parcel.writeString(originalLanguage)
        parcel.writeList(genreIds)
        parcel.writeString(posterPath)
        parcel.writeList(originCountry)
        parcel.writeString(backdropPath)
        parcel.writeString(originalName)
        parcel.writeValue(popularity)
        parcel.writeValue(voteAverage)
        parcel.writeValue(id)
        parcel.writeValue(voteCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    protected constructor(parcel: Parcel) {
        key = parcel.readString()
        name = parcel.readString()
        firstAirDate = parcel.readString()
        overview = parcel.readString()
        originalLanguage = parcel.readString()
        genreIds = ArrayList()
        parcel.readList(genreIds, Int::class.java.classLoader)
        posterPath = parcel.readString()
        originCountry = ArrayList()
        parcel.readList(originCountry, Int::class.java.classLoader)
        backdropPath = parcel.readString()
        originalName = parcel.readString()
        popularity = parcel.readValue(Double::class.java.classLoader) as Double?
        voteAverage = parcel.readValue(Double::class.java.classLoader) as Double?
        id = parcel.readValue(Int::class.java.classLoader) as Int?
        voteCount = parcel.readValue(Int::class.java.classLoader) as Int?
    }

    companion object CREATOR : Parcelable.Creator<TvShows> {
        override fun createFromParcel(parcel: Parcel): TvShows {
            return TvShows(parcel)
        }

        override fun newArray(size: Int): Array<TvShows?> {
            return arrayOfNulls(size)
        }
    }
}