package za.ac.iie.mytamagotchi

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Playtime : AppCompatActivity() {

    // Constants for time calculation and attribute limits
    private val daysInMillis = 24 * 60 * 60 * 1000
    private val MAX_HUNGER = 100
    private val MAX_CLEANLINESS = 100
    private val MAX_HAPPINESS = 100

    // Pet attributes and last fed time
    private var hunger = 50
    private var cleanliness = 50
    private var happiness = 50
    private var lastFedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_playtime)

        // Load last fed time from SharedPreferences
        lastFedTime = loadLastFedTime()

        // Initialize UI elements and set click listeners
        val feedButton = findViewById<Button>(R.id.feedButton)
        val cleanButton = findViewById<Button>(R.id.cleanButton)
        val playButton = findViewById<Button>(R.id.playButton)
        val chillingImageView = findViewById<ImageView>(R.id.chillingImageView)
        findViewById<TextView>(R.id.hungerStatTextView)
        findViewById<TextView>(R.id.cleanStatTextView)
        findViewById<TextView>(R.id.happyStatTextView)

        // Clean button click listener
        cleanButton.setOnClickListener {
            // Show showering animation
            chillingImageView.setImageResource(R.drawable.snoopy_showering)
            android.os.Handler().postDelayed({
                chillingImageView.setImageResource(R.drawable.snoopy_chilling)
            }, 3000) // Revert to chilling image after 3 seconds

            // Set cleanliness to maximum
            cleanliness = MAX_CLEANLINESS

            // Update UI
            updateStatsTextViews()
        }

        // Play button click listener
        playButton.setOnClickListener {
            // Show happy animation
            chillingImageView.setImageResource(R.drawable.snoopy_happy)
            android.os.Handler().postDelayed({
                chillingImageView.setImageResource(R.drawable.snoopy_chilling)
            }, 3000) // Revert to chilling image after 3 seconds

            // Check feeding status and set happiness to maximum
            checkFeedingStatus()
            happiness = MAX_HAPPINESS

            // Update UI
            updateStatsTextViews()
        }

        // Feed button click listener
        feedButton.setOnClickListener {
            // Show eating animation
            chillingImageView.setImageResource(R.drawable.snoopy_eating)
            android.os.Handler().postDelayed({
                chillingImageView.setImageResource(R.drawable.snoopy_chilling)
            }, 3000) // Revert to chilling image after 3 seconds

            // Set hunger to maximum
            hunger = MAX_HUNGER

            // Update UI
            updateStatsTextViews()
        }

        // Apply system window insets to main layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Check feeding status and update UI
        checkFeedingStatus()
        updateStatsTextViews()
    }

    // Update TextViews displaying pet's status
    private fun updateStatsTextViews() {
        findViewById<TextView>(R.id.hungerStatTextView).text = "$hunger%"
        findViewById<TextView>(R.id.cleanStatTextView).text = "$cleanliness%"
        findViewById<TextView>(R.id.happyStatTextView).text = "$happiness%"
    }

    // Check if pet was fed today and update hunger level
    private fun checkFeedingStatus() {
        val currentTime = System.currentTimeMillis()
        val timeSinceLastFed = currentTime - lastFedTime

        if (timeSinceLastFed >= daysInMillis) {
            hunger -= 10
            if (hunger < 0) hunger = 0
            updateStatsTextViews()
        }
    }

    // Load last fed time from SharedPreferences
    private fun loadLastFedTime(): Long {
        return getSharedPreferences("PetPrefs", Context.MODE_PRIVATE).getLong("lastFedTime", 0)
    }

}