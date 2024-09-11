package com.example.wordle

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var guessCount = 0
    private var selectedCategory = "regular"  // Default category
    private lateinit var targetWord: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the word with the selected category
        targetWord = FourLetterWordList.getRandomFourLetterWord(selectedCategory)

        // Get references to UI elements
        val guessInput = findViewById<EditText>(R.id.etGuessInput)
        val submitButton = findViewById<Button>(R.id.btnSubmitGuess)
        val resultTextView = findViewById<TextView>(R.id.tvResults)
        val btnReset = findViewById<Button>(R.id.btnReset)

        // Handle the Submit button click
        submitButton.setOnClickListener {
            val userGuess = guessInput.text.toString().uppercase()

            // Check if the guess is exactly 4 letters long
            if (userGuess.length != 4) {
                Toast.makeText(this, "Please enter exactly 4 letters.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if the guess contains only alphabetical characters
            if (!userGuess.matches(Regex("^[A-Z]+$"))) {
                Toast.makeText(this, "Please enter only alphabetical characters (A-Z).", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check the guess and display the result
            val result = checkGuess(userGuess, targetWord)
            resultTextView.text = result

            // Increment guess count
            guessCount++

            // Disable submit and show reset after 3 guesses
            if (guessCount == 3) {
                submitButton.isEnabled = false
                btnReset.visibility = View.VISIBLE  // Show the Reset button
                Toast.makeText(this, "No more guesses left! The word was $targetWord.", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle the Reset button click
        btnReset.setOnClickListener {
            resetGame()  // Reset the game logic (defined below)
        }
    }

    // Define the resetGame function
    private fun resetGame() {
        guessCount = 0  // Reset the guess count
        targetWord = FourLetterWordList.getRandomFourLetterWord(selectedCategory)  // Get a new word
        findViewById<TextView>(R.id.tvResults).text = ""  // Clear the results
        findViewById<EditText>(R.id.etGuessInput).text.clear()  // Clear the input field
        findViewById<Button>(R.id.btnSubmitGuess).isEnabled = true  // Re-enable the submit button
        findViewById<Button>(R.id.btnReset).visibility = View.GONE  // Hide the reset button
        Toast.makeText(this, "Game reset! Guess the new word!", Toast.LENGTH_SHORT).show()
    }

    // Function to check the user's guess and return feedback
    private fun checkGuess(guess: String, target: String): String {
        val result = StringBuilder()

        for (i in guess.indices) {
            when {
                guess[i] == target[i] -> result.append('O')  // Correct letter in the correct position
                target.contains(guess[i]) -> result.append('+')  // Correct letter but in the wrong position
                else -> result.append('X')  // Incorrect letter
            }
        }

        return result.toString()  // Return the result string as feedback
    }
}