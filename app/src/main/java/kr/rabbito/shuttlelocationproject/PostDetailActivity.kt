package kr.rabbito.shuttlelocationproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kr.rabbito.shuttlelocationproject.data.Comment
import kr.rabbito.shuttlelocationproject.data.DeleteDialog
import kr.rabbito.shuttlelocationproject.data.Post
import kr.rabbito.shuttlelocationproject.databinding.ActivityPostDetailBinding
import kr.rabbito.shuttlelocationproject.function.hashSHA256
import java.text.SimpleDateFormat
import java.util.*

class PostDetailActivity : AppCompatActivity() {
    private var mBinding: ActivityPostDetailBinding? = null
    private val binding get() = mBinding!!
    val TAG: String = "TAG"
    //private var time = SimpleDateFormat("yyyy년 MM월 dd일").format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityPostDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)
        overridePendingTransition(0, 0)
        Log.d(TAG, "PostDetailActivity called()")


        val shared = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        //저장되어 있는 UserMode 값 getString / default Value : User
        val loginMode = shared.getString("UserMode", "User")

        //UserMode : User 일 경우 comment 관련 INVISIBLE
        if (loginMode != "Manager") {
            binding.postdetailEtComment.visibility = View.INVISIBLE
            binding.postdetailBtnComment.visibility = View.INVISIBLE
        }

        //intent에 담긴 bundle -> post에 풀기
        val bundle = intent.extras
        val post = bundle!!.getParcelable<Post>("selectedPost")!!

        val comment = Comment()

        //Toast.makeText(this,"ClcikListener 실행 완료 ",Toast.LENGTH_SHORT).show()
        val commentRef = FirebaseDatabase.getInstance().getReference("Community/Comment")
        val postRef = FirebaseDatabase.getInstance().getReference("Community/Post")
        //key == commentId
        val commentKey = commentRef.push().key!!


        // post_detail.xml
        binding.postdetailTvTitle.text = post.postTitle
        binding.postdetailTvContent.text = post.postContent
        binding.postdetailTvDate.text = post.postDate
        binding.postdetailBtnCommentdelete.visibility = View.INVISIBLE
        //날짜 받아와서 변환하기






        //comment 없을 경우 NullPointerException 예외 처리?

        //TODO(
        Firebase.database.getReference("Community/Comment").child(post.postCommentId).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val comment = task.result.getValue(Comment::class.java)
                    if (comment?.comment != null) {
                        binding.postdetailTvCommentdetail.text = "답변 내용 : ${comment.comment}"
                        binding.postdetailBtnCommentdelete.visibility = View.VISIBLE
                    } else {
                        binding.postdetailTvCommentdetail.visibility = View.INVISIBLE
                        binding.postdetailBtnCommentdelete.visibility = View.INVISIBLE

                    }
                } else {
                    binding.postdetailTvCommentdetail.visibility = View.INVISIBLE
                    binding.postdetailBtnCommentdelete.visibility = View.INVISIBLE

                }
            }

        // 게시물 수정하기 삭제하기 ->
        binding.postdetailBtnDelete.setOnClickListener {
            val dialog = DeleteDialog(this@PostDetailActivity)
            dialog.showDialog()
            dialog.setOnClickListner(object : DeleteDialog.ButtonClickListener {
                override fun onClicked(text: String) {
                    val inputPassword = text.hashSHA256()
                    Toast.makeText(this@PostDetailActivity, inputPassword, Toast.LENGTH_SHORT).show()
                    //입력한 비밀번호와 post에 저장되어 있는 게시물 비밀번호 같을 경우
                    if (inputPassword == post.postPassword) {
                        //post 삭제
                        postRef.child(post.postId).removeValue()
                        //comment 삭제
                        commentRef.child(post.postCommentId).removeValue()
                        Toast.makeText(this@PostDetailActivity, "삭제 완료", Toast.LENGTH_SHORT).show()
                        finish()
                    } else { // 게시물 비밀번호 다를 경우
                        Toast.makeText(this@PostDetailActivity, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            })

        }
        binding.postdetailBtnEdit.setOnClickListener {
            //deleteDialog 재 사용 -> editText.setText() 사용
            val dialog = DeleteDialog(this@PostDetailActivity)

            dialog.showDialog()
            dialog.setOnClickListner(object : DeleteDialog.ButtonClickListener {
                override fun onClicked(text: String) {
                    val inputPassword = text.hashSHA256()
                    //Toast.makeText(this@PostDetailActivity, inputPassword, Toast.LENGTH_SHORT).show()
                    //입력한 비밀번호와 post에 저장되어 있는 게시물 비밀번호 같을 경우
                    if (inputPassword == post.postPassword) {
                        val intent = Intent(this@PostDetailActivity, PostActivity::class.java)
                        intent.putExtra("post", post)
                        intent.putExtra("PostMode", "Edit")
                        intent.putExtra("PostId", post.postId)
                        intent.putExtra("PostPassword", post.postPassword)
                        startActivity(intent)
                        //post 삭제
                        finish()
                    } else { // 게시물 비밀번호 다를 경우
                        Toast.makeText(this@PostDetailActivity, "게시물 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }


        //key == commentId / firebase upload
        binding.postdetailBtnComment.setOnClickListener {
            postRef.child(post.postId).child("postCommentId").setValue(commentKey)
            comment.postId = post.postId
            comment.comment = binding.postdetailEtComment.text.toString()
            comment.commentId = commentKey
            commentRef.child(commentKey).setValue(comment)
            Toast.makeText(this, "Comment 등록 완료", Toast.LENGTH_SHORT).show()
            finish()
        }
        binding.postdetailBtnCommentdelete.setOnClickListener {
            if (binding.postdetailTvCommentdetail.text.toString() != ""){
                commentRef.child(post.postCommentId).removeValue()
                Toast.makeText(this, "Comment 삭제 완료", Toast.LENGTH_SHORT).show()
                finish()

            }
        }

        binding.postdetailBtnBack.setOnClickListener {
            finish()
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}