package kr.rabbito.shuttlelocationproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kr.rabbito.shuttlelocationproject.data.Post
import kr.rabbito.shuttlelocationproject.databinding.ActivityPostBinding
import java.text.SimpleDateFormat

class PostActivity : AppCompatActivity() {
    val TAG: String = "로그"

    private var mBinding: ActivityPostBinding? = null
    private val binding get() = mBinding!!

     var postData = Post()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = FirebaseDatabase.getInstance()

        val ref :DatabaseReference= database.getReference("Community")
        //val group = "Post"

        binding.postBtnUpload.setOnClickListener {

            with(binding) {
                postData.postContent = postEtvTitle.text.toString()
                postData.postTitle = postEtvContent.text.toString()
                postData.postPassword = postEtvPassword.text.toString()
                postData.postId = ""
//                var currentTime = System.currentTimeMillis()
//                val dataFormat1 = SimpleDateFormat("YY-MM-DD-E")
                postData.postDate = "1월1일"//dataFormat1.format(currentTime)
            }
            val postId = ref.push().key!!
            ref.child(postId).setValue(postData)
            Toast.makeText(this@PostActivity, "ref.setValue 완료", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, CommunityActivity::class.java)
            startActivity(intent)

        }
    }
}
