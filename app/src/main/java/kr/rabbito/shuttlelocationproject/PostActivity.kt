package kr.rabbito.shuttlelocationproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.ktx.Firebase
import kr.rabbito.shuttlelocationproject.data.Post
import kr.rabbito.shuttlelocationproject.databinding.ActivityPostBinding
import kr.rabbito.shuttlelocationproject.function.hashSHA256

class PostActivity : AppCompatActivity() {
    private var mBinding: ActivityPostBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPostBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val mode = intent.getStringExtra("PostMode") ?: "temp"
        if(mode == "Edit"){
            binding.postBtnPost.visibility = View.INVISIBLE
            binding.postBtnEdit.visibility = View.VISIBLE

            val bundle = intent.extras
            val tmpPost = bundle?.getParcelable<Post>("post")
            with(binding){
                postEtTitle.setText(tmpPost?.postTitle)
                postEtContent.setText(tmpPost?.postContent)
            }
        }
        //post -> userInput Value 담을 객체
        val post = Post()
        val ref = FirebaseDatabase.getInstance().getReference("Community/Post")

        // Key = PostKey
        val key = ref.push().key!!

        // user input -> Post -> Firebase 등록
        binding.postBtnPost.setOnClickListener {
            post.postTitle = binding.postEtTitle.text.toString()
            post.postContent = binding.postEtContent.text.toString()
            //password -> 암호화하여 등록
            post.postPassword = binding.postEtPassword.text.toString().hashSHA256()
            post.postDate = System.currentTimeMillis().toString()
            post.postId = key
            // post.postCommentId -> 임시 등록
            post.postCommentId = ""

            //Firebase upload
            ref.child(key).setValue(post)
            finish()
        }

        binding.postBtnEdit.setOnClickListener {
            val tmpPostPassword = intent.getStringExtra("PostPassword")
            val tmpPostId = intent.getStringExtra("PostId")

            post.postTitle = binding.postEtTitle.text.toString()
            post.postContent = binding.postEtContent.text.toString()
            //password -> 암호화하여 등록
            post.postPassword = binding.postEtPassword.text.toString().hashSHA256()
            post.postDate = System.currentTimeMillis().toString()
            post.postId = tmpPostId!!

            if (post.postPassword == tmpPostPassword){
                ref.child(post.postId).setValue(post)
                val intent = Intent(this,CommunityActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()

            }
        }


    }
}