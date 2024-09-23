package com.example.wordle

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
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
    private var streakCount = 0  // Initialize streak count

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
        val streakTextView = findViewById<TextView>(R.id.streakTextView)

        streakTextView.text = "Streak: $streakCount"

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

            // If the user guessed correctly
            if (userGuess == targetWord) {
                streakCount++  // Increment the streak count
                streakTextView.text = "Streak: $streakCount"
                Toast.makeText(this, "You guessed the word!", Toast.LENGTH_LONG).show()
                findViewById<View>(R.id.mainLayout).setBackgroundColor(getColor(R.color.correct_position))  // Visual change
                submitButton.isEnabled = false
                btnReset.visibility = View.VISIBLE  // Show reset button
            }
            // If 3 guesses are made
            else if (guessCount == 3) {
                streakCount = 0  // Reset streak if lost
                streakTextView.text = "Streak: $streakCount"
                submitButton.isEnabled = false
                resultTextView.text = "The word was $targetWord"
                Toast.makeText(this, "No more guesses left! The word was $targetWord", Toast.LENGTH_LONG).show()
                btnReset.visibility = View.VISIBLE
            }
        }

        // Handle the Reset button click
        btnReset.setOnClickListener {
            resetGame()  // Reset the game logic
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
        findViewById<View>(R.id.mainLayout).setBackgroundColor(getColor(R.color.defaultBackground))  // Reset background color
        Toast.makeText(this, "Game reset! Guess the new word!", Toast.LENGTH_SHORT).show()
    }

    // Function to check the user's guess and return feedback with colors
    private fun checkGuess(guess: String, target: String): SpannableString {
        val spannableResult = SpannableString(guess)

        for (i in guess.indices) {
            when {
                guess[i] == target[i] -> {
                    // Correct letter in correct position (Green)
                    spannableResult.setSpan(
                        ForegroundColorSpan(getColor(R.color.correct_position)),
                        i, i + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                target.contains(guess[i]) -> {
                    // Correct letter in wrong position (Yellow)
                    spannableResult.setSpan(
                        ForegroundColorSpan(getColor(R.color.correct_letter)),
                        i, i + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                else -> {
                    // Incorrect letter (Red)
                    spannableResult.setSpan(
                        ForegroundColorSpan(getColor(R.color.incorrect_letter)),
                        i, i + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
        }
        return spannableResult
    }
}
