package kr.rabbito.shuttlelocationproject

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kr.rabbito.shuttlelocationproject.data.ManagerDialog
import kr.rabbito.shuttlelocationproject.databinding.ActivitySettingBinding
import kr.rabbito.shuttlelocationproject.function.hashSHA256

class SettingActivity : AppCompatActivity() {
    private var mBinding: ActivitySettingBinding? = null
    private val binding get() = mBinding!!

    val TAG: String = "TAG"
    val pwdPath = listOf("1", "2", "3", "4", "5", "6")
    val PWD = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySettingBinding.inflate(layoutInflater)

        setContentView(binding.root)
        overridePendingTransition(0, 0)


        val shared = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        val editor = shared.edit()

        var loginMode = shared.getString("UserMode", "User")

        if (loginMode == "Manager") {
            binding.settingTvAdmin.text = "관리자 로그아웃"
            binding.settingTvAdminSub.text = "관리자 권한을 해제합니다."
        } else {
            binding.settingTvAdmin.text = "관리자 로그인"
            binding.settingTvAdminSub.text = "게시물 답변 작성 권한을 획득합니다."
        }

        for (i in pwdPath.indices) {
            //루프 돌며 이미 등록되어 있는 관리자 비밀와 inputPassword 비교
            Firebase.database.getReference("Manager").child("${i + 1}").get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val dbPassword = task.result.value
                        Log.d(TAG, "task.result.value : ${task.result.value}")
                        PWD.add(dbPassword.toString())
                    }
                }
        }
        //Log.d(TAG,"PWD SIZE : ${PWD.size}")

        //관리자 로그인 버튼 클릭
        binding.settingClSettingAdmin.setOnClickListener {
            if (loginMode == "Manager") {
                editor.putString("UserMode", "User")
                loginMode = "User"
                editor.apply()

                Toast.makeText(
                    this@SettingActivity,
                    "로그아웃 되었습니다.",
                    Toast.LENGTH_SHORT
                ).show()

                binding.settingTvAdmin.text = "관리자 로그인"
                binding.settingTvAdminSub.text = "게시물 답변 작성 권한을 획득합니다."
            } else {
                val dialog = ManagerDialog(this)
                dialog.showDialog()
                dialog.setOnClickListner(object : ManagerDialog.ButtonClickListener {
                    override fun onClicked(text: String) {
                        //inputPassword : 사용자가 입력한 PWD
                        val inputPassword = text.hashSHA256()
                        //Log.d(TAG,"inputPassword : $inputPassword")

                        //Log.d(TAG,"hashPWD : ${inputPassword.hashSHA256()}")

                        //pwdPath : Firebase에 저장되어 있는 key 값
                        //dbPassword : 이미 등록되어 있는 PWD.hashSHA226() 값


                        //사용자 입력 비밀번호가 Manager PWD 리스트에 있는지 검사
                        if (inputPassword in PWD) {
                            editor.putString("UserMode", "Manager")
                            editor.apply()
                            loginMode = "Manager"

                            Toast.makeText(
                                this@SettingActivity,
                                "관리자로 로그인 되었습니다.",
                                Toast.LENGTH_SHORT
                            )
                                .show()

                            binding.settingTvAdmin.text = "관리자 로그아웃"
                            binding.settingTvAdminSub.text = "관리자 권한을 해제합니다."
                        } else {
                            Toast.makeText(
                                this@SettingActivity,
                                "비밀번호가 일치하지 않습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                })
            }
        }

        binding.settingClSettingInfo.setOnClickListener {
            val dialogView = View.inflate(this, R.layout.info_dialog, null)
            val info = AlertDialog.Builder(this)
            val dlg = info.create()
            val info_btn = dialogView.findViewById<TextView>(R.id.infodialog_btn_cancel)
            info_btn.setOnClickListener { dlg.dismiss() }
            dlg.setView(dialogView)
            dlg.show()
        }


        binding.settingBtnBack.setOnClickListener {
            finish()
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}