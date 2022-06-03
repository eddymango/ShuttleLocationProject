package kr.rabbito.shuttlelocationproject

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import kr.rabbito.shuttlelocationproject.data.Post
import kr.rabbito.shuttlelocationproject.databinding.ActivityPostBinding
import kr.rabbito.shuttlelocationproject.function.hashSHA256

class PostActivity : AppCompatActivity() {
    private var mBinding: ActivityPostBinding? = null
    private val binding get() = mBinding!!
    private val TAG="TAG"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPostBinding.inflate(layoutInflater)

        setContentView(binding.root)
        overridePendingTransition(0, 0)

        val mode = intent.getStringExtra("PostMode") ?: "temp"
        if(mode == "Edit"){
            binding.postTvSubject.text = "게시글 수정"

            binding.postBtnPost.visibility = View.INVISIBLE
            binding.postBtnEdit.visibility = View.VISIBLE
            val bundle = intent.extras
            val tmpPost = bundle?.getParcelable<Post>("post")
            with(binding){
                postEtTitle.setText(tmpPost?.postTitle)
                postEtContent.setText(tmpPost?.postContent)
                //temp.postPassword = 암호화 알고리즘 적용된 비밀번호
                postEtPassword.setText(intent.getStringExtra("PostPassword"))
                Log.d(TAG,"postEtPassword : $postEtPassword")
            }
        }

        //post -> userInput Value 담을 객체
        val post = Post()
        val ref = FirebaseDatabase.getInstance().getReference("Community/Post")

        binding.postEtContent.addTextChangedListener(object : TextWatcher {
            var prev = ""

            override fun afterTextChanged(p0: Editable?) {
                if (binding.postEtContent.lineCount >= 18) {
                    binding.postEtContent.setText(prev)
                    binding.postEtContent.setSelection(binding.postEtContent.length())
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                prev = p0.toString()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        // user input -> Post -> Firebase 등록
        binding.postBtnPost.setOnClickListener {
            if (binding.postEtTitle.text.toString() == "") {
                Toast.makeText(this, "제목은 반드시 입력해야 합니다.", Toast.LENGTH_SHORT).show()
            } else {
                // 체크 박스
                val dialogView = View.inflate(this, R.layout.check_dialog, null)
                val check = AlertDialog.Builder(this)
                val dlg = check.create()
                val check_ok_btn = dialogView.findViewById<TextView>(R.id.checkdialog_btn_ok)
                val check_cancel_btn =
                    dialogView.findViewById<TextView>(R.id.checkdialog_btn_cancel)
                val check_tv = dialogView.findViewById<TextView>(R.id.checkdialog_tv_title)
                check_tv.text = "게시글을 등록하시겠습니까?"
                check_cancel_btn.setOnClickListener { dlg.dismiss() }
                // 확인 버튼
                check_ok_btn.setOnClickListener {
                    val key = ref.push().key!!

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
                dlg.setView(dialogView)
                dlg.show()
            }
        }

        binding.postBtnEdit.setOnClickListener {
            val dialogView = View.inflate(this, R.layout.check_dialog, null)
            val check = AlertDialog.Builder(this)
            val dlg = check.create()
            val check_ok_btn = dialogView.findViewById<TextView>(R.id.checkdialog_btn_ok)
            val check_cancel_btn =
                dialogView.findViewById<TextView>(R.id.checkdialog_btn_cancel)
            val check_tv = dialogView.findViewById<TextView>(R.id.checkdialog_tv_title)
            check_tv.text = "게시글을 수정하시겠습니까?"
            check_cancel_btn.setOnClickListener { dlg.dismiss() }
            // 확인 버튼
            check_ok_btn.setOnClickListener {
                val tmpPostId = intent.getStringExtra("PostId")

                post.postTitle = binding.postEtTitle.text.toString()
                post.postContent = binding.postEtContent.text.toString()
                //password -> 암호화하여 등록
                //사용자가 postActivity에서 비밀번호 새로 입력할 경우 다시 등록하도록 함
                post.postPassword = binding.postEtPassword.text.toString().hashSHA256()
                post.postDate = System.currentTimeMillis().toString()
                post.postId = tmpPostId!!

                ref.child(post.postId).setValue(post)
                intent.putExtra("EditPost",post)
                Log.d(TAG,"EditPost password : $post.")
                finish()
            }
            dlg.setView(dialogView)
            dlg.show()
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}