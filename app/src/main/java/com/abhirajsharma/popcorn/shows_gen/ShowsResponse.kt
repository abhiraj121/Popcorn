package com.abhirajsharma.popcorn.shows_gen

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ShowsResponse (
    var results: List<TvShows>? = null,
    var page: Int = 0,
    @SerializedName("total_results")
    var totalResults: Int = 0,
    @SerializedName("total_pages")
    var totalPages: Int = 0
):Parcelable {
    constructor(parcel: Parcel) : this(
        TODO("results"),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(page)
        parcel.writeInt(totalResults)
        parcel.writeInt(totalPages)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShowsResponse> {
        override fun createFromParcel(parcel: Parcel): ShowsResponse {
            return ShowsResponse(parcel)
        }

        override fun newArray(size: Int): Array<ShowsResponse?> {
            return arrayOfNulls(size)
        }
    }
}