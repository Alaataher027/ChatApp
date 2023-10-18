package com.example.chatapp.model

import com.example.chatapp.R

data class Category(
    val id: String? = null,
    val name: String? = null,
    val imageId: Int? = R.drawable.sports
) {
    companion object {
        const val MUSIC = "MUSIC"
        const val SPORTS = "SPORTS"
        const val MOVIES = "MOVIES"
        fun fromId(categoryId: String): Category {
            return when (categoryId) {
                MUSIC -> {
                    return Category(MUSIC, "Music", R.drawable.music)
                }

                SPORTS -> Category(SPORTS, "Sports", R.drawable.sports)
                else -> Category(MOVIES, "Movies", R.drawable.movies)
            }
        }

        fun getCategoriesList(): List<Category> {
            return listOf(fromId(MUSIC), fromId(SPORTS), fromId(MOVIES))
        }

    }
}