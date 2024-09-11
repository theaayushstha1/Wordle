package com.example.wordle

object FourLetterWordList {
    private val regularWords = listOf("GAME", "WORD", "PLAY", "CODE")
    private val sportWords = listOf("BALL", "GOAL", "RACE", "SWIM")
    private val techWords = listOf("JAVA", "KOTL", "HTML", "CODE")

    // Function to get random word from the selected category
    fun getRandomFourLetterWord(category: String): String {
        return when (category) {
            "sports" -> sportWords.random()
            "tech" -> techWords.random()
            else -> regularWords.random()
        }
    }
}