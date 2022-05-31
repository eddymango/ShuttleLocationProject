package kr.rabbito.shuttlelocationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.rabbito.shuttlelocationproject.databinding.ActivityCommunityBinding
import kr.rabbito.shuttlelocationproject.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private var mBinding: ActivityDetailBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)
        overridePendingTransition(0, 0)

        binding.detailBtnBack.setOnClickListener {
            finish()
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}