package kr.rabbito.shuttlelocationproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import kr.rabbito.shuttlelocationproject.databinding.ActivityLoadingBinding
import kr.rabbito.shuttlelocationproject.databinding.ActivityMainBinding

class LoadingActivity : AppCompatActivity() {
    private var mBinding: ActivityLoadingBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoadingBinding.inflate(layoutInflater)

        setContentView(binding.root)
        overridePendingTransition(0, 0)

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

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}