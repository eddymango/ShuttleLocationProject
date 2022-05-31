package kr.rabbito.shuttlelocationproject

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kr.rabbito.shuttlelocationproject.data.Comment
import kr.rabbito.shuttlelocationproject.data.ManagerDialog
import kr.rabbito.shuttlelocationproject.databinding.ActivityMainBinding
import kr.rabbito.shuttlelocationproject.databinding.ActivitySettingBinding
import kr.rabbito.shuttlelocationproject.function.hashSHA256

class SettingActivity : AppCompatActivity() {
    private var mBinding: ActivitySettingBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySettingBinding.inflate(layoutInflater)

        setContentView(binding.root)
        overridePendingTransition(0, 0)

        val shared = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        val editor = shared.edit()


        binding.button.setOnClickListener {
            val dialog = ManagerDialog(this)
            dialog.showDialog()
            dialog.setOnClickListner(object:ManagerDialog.ButtonClickListener{
                override fun onClicked(text:String){

                    val inputPassword = text

                    //DB에 저장된 Password 일치하는지 조회
                    Firebase.database.getReference("Manager").
                    get().addOnCompleteListener {task ->
                        if (task.isSuccessful){
                            val dbPassword = task.result.value
                            // 답변 내용 어떤 형식으로 출력?
                            // 답변 없을 경우 게시판에 답변 어떻게 노출?
                            if (inputPassword == dbPassword){
                                editor.putString("mode","Manager")
                                editor.apply()
                            }
                            else{
                                Toast.makeText(this@SettingActivity,"비밀번호를 잘못 입력하셨습니다.",Toast.LENGTH_SHORT).show()

                            }
                        }
                    }
                }
            })
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