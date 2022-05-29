package kr.rabbito.shuttlelocationproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startLoading()
    }

    private fun startLoading() {
        val handler = Handler()
        handler.postDelayed({
            val intent  = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
        }, 1000)
    }
}