package com.project.sfmd_project
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import com.airbnb.lottie.LottieAnimationView
class Splash_Screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutParams = WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        }
        window.attributes = layoutParams

        setContentView(R.layout.splash_screen)

        Handler().postDelayed({
            // Intent to start the LoginPage activity
            val loginIntent = Intent(this, Login::class.java)
            startActivity(loginIntent)
            finish() // Destroy this activity so the user can't return to it
        }, 5000)


    }

}