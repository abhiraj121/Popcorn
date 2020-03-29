package com.abhirajsharma.popcorn.Search_gen

import android.os.Parcel
import android.os.Parcelable

class SearchItem : Parcelable{
    var adult: Boolean = false
    var backdrop_path: Any ?=null
    var genre_ids: List<Int> ?= null
    var id: Int?= null
    var media_type: String?= null
    var original_language: String?= null
    var original_title: String?= null
    var overview: String?= null
    var popularity: Double?= null
    var poster_path: String?= null
    var release_date: String?= "Not Available"
    var video: Boolean = false
    var vote_average: Double?= null
    var vote_count: Int?= null

    constructor(parcel: Parcel) {
        adult = parcel.readByte() != 0.toByte()
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        media_type = parcel.readString()
        original_language = parcel.readString()
        original_title = parcel.readString()
        overview = parcel.readString()
        popularity = parcel.readValue(Double::class.java.classLoader) as? Double
        poster_path = parcel.readString()
        release_date = parcel.readString()
        video = parcel.readByte() != 0.toByte()
        vote_average = parcel.readValue(Double::class.java.classLoader) as? Double
        vote_count = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (adult) 1 else 0)
        parcel.writeValue(id)
        parcel.writeString(media_type)
        parcel.writeString(original_language)
        parcel.writeString(original_title)
        parcel.writeString(overview)
        parcel.writeValue(popularity)
        parcel.writeString(poster_path)
        parcel.writeString(release_date)
        parcel.writeByte(if (video) 1 else 0)
        parcel.writeValue(vote_average)
        parcel.writeValue(vote_count)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchItem> {
        override fun createFromParcel(parcel: Parcel): SearchItem {
            return SearchItem(parcel)
        }

        override fun newArray(size: Int): Array<SearchItem?> {
            return arrayOfNulls(size)
        }
    }
}