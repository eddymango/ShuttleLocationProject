package kr.rabbito.shuttlelocationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
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
            val dialogView = View.inflate(this, R.layout.schedule_dialog, null)
            val schedule = AlertDialog.Builder(this)
            val dlg = schedule.create()
            val schedule_btn = dialogView.findViewById<TextView>(R.id.scheduledialog_btn_cancel)
            schedule_btn.setOnClickListener { dlg.dismiss() }
            dlg.setView(dialogView)
            dlg.show()
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