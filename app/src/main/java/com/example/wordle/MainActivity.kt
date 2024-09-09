package com.example.wordle

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    // Track the number of guesses
    private var guessCount = 0
    // The randomly selected word
    private lateinit var targetWord: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the target word
        targetWord = FourLetterWordList.getRandomFourLetterWord()

        // Get references to UI elements
        val guessInput = findViewById<EditText>(R.id.etGuessInput)
        val submitButton = findViewById<Button>(R.id.btnSubmitGuess)
        val resultTextView = findViewById<TextView>(R.id.tvResults)

        // Set up the button click listener
        submitButton.setOnClickListener {
            val userGuess = guessInput.text.toString().uppercase()

            // Check if input is a 4-letter word
            if (userGuess.length != 4) {
                Toast.makeText(this, "Please enter a 4-letter word", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Increment guess count and check if user still has guesses left
            guessCount++
            if (guessCount <= 3) {
                // Check the guess and display the result
                val result = checkGuess(userGuess, targetWord)
                resultTextView.text = result

                // Disable the button after 3 guesses
                if (guessCount == 3) {
                    submitButton.isEnabled = false
                    // Updated line to use string resources for result message
                    resultTextView.text = getString(R.string.result_message, targetWord, result)
                    Toast.makeText(this, "No more guesses left!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Function to check the user's guess
    private fun checkGuess(guess: String, target: String): String {
        val result = StringBuilder()

        for (i in guess.indices) {
            when {
                guess[i] == target[i] -> result.append('O') // Correct letter in correct place
                target.contains(guess[i]) -> result.append('+') // Correct letter, wrong place
                else -> result.append('X') // Wrong letter
            }
        }

        return result.toString()
    }
}