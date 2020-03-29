package com.abhirajsharma.popcorn.trailers

import com.google.gson.annotations.SerializedName


data class Trailer(
    @field:SerializedName("key") var key: String,
    @field:SerializedName("name") var name: String
)
