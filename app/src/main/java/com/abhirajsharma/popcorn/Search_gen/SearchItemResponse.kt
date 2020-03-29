package com.abhirajsharma.popcorn.Search_gen

data class SearchItemResponse(
    val page: Int,
    val results: List<SearchItem>,
    val total_pages: Int,
    val total_results: Int
)