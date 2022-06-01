package kr.rabbito.shuttlelocationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
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

        binding.detailBtnSchedule.setOnClickListener{
            var dialogView = View.inflate(this, R.layout.schedule_dialog, null)
            var schedule = AlertDialog.Builder(this)
            schedule.setTitle("셔틀 노선도")
            schedule.setView(dialogView)
            Log.d("loc", "loc")
            schedule.setPositiveButton("확인", null)
            schedule.show()
        }

        binding.detailBtnBack.setOnClickListener {
            finish()
        }


    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}