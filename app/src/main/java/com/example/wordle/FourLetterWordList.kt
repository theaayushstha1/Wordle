package com.example.wordle  // Ensure this package matches your project package

object FourLetterWordList {
    private val fourLetterWords = listOf(
        "GAME", "WORD", "PLAY", "CODE", "JAVA", "KOTL", "STAY", "LIST", "BEST", "GOOD"
    )

    fun getRandomFourLetterWord(): String {
        return fourLetterWords.random()
    }
}