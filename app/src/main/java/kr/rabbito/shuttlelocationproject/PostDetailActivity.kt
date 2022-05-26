package kr.rabbito.shuttlelocationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kr.rabbito.shuttlelocationproject.data.Comment
import kr.rabbito.shuttlelocationproject.data.DeleteDialog
import kr.rabbito.shuttlelocationproject.data.Post
import kr.rabbito.shuttlelocationproject.databinding.ActivityPostDetailBinding
import kr.rabbito.shuttlelocationproject.function.hashSHA256

class PostDetailActivity : AppCompatActivity() {
    private var mBinding: ActivityPostDetailBinding? = null
    private val binding get() = mBinding!!
    val TAG :String = "TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityPostDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)
        Log.d(TAG,"PostDetailActivity called()")


        //intent에 담긴 bundle -> post에 풀기
        val bundle = intent.extras
        val post = bundle!!.getParcelable<Post>("selectedPost")!!

        val comment = Comment()

        //Toast.makeText(this,"ClcikListener 실행 완료 ",Toast.LENGTH_SHORT).show()
        val commentRef = FirebaseDatabase.getInstance().getReference("Community/Comment")
        val postRef = FirebaseDatabase.getInstance().getReference("Community/Post")
        //key == commentId
        val commentKey = commentRef.push().key!!

        //deleteBtn -> deleteDialog show()
        binding.postdetailBtnDelete.setOnClickListener {
            val dialog = DeleteDialog(this)
            dialog.showDialog()
            dialog.setOnClickListner(object:DeleteDialog.ButtonClickListener{
                override fun onClicked(text: String) {
                    val inputPassword = text.hashSHA256()
                    Toast.makeText(this@PostDetailActivity, inputPassword, Toast.LENGTH_SHORT).show()
                    if (inputPassword == post.postPassword ){
                        //post 삭제
                        postRef.child(post.postId).removeValue()
                        //comment 삭제
                        commentRef.child(post.postCommentId).removeValue()
                        Toast.makeText(this@PostDetailActivity, "삭제 완료", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else{
                        Toast.makeText(this@PostDetailActivity, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }


        // post_detail.xml
        binding.postdetailTvTitle.text = post.postTitle
        binding.postdetailTvContent.text = post.postContent
        binding.postdetailTvDate.text = post.postDate
        binding.postdetailTvPassword.text = post.postPassword


        //comment 없을 경우 NullPointerException 예외 처리?

        //TODO(
        Firebase.database.getReference("Community/Comment").
            child(post.postCommentId).get().addOnCompleteListener {task ->
            if (task.isSuccessful){
                val comment = task.result.getValue(Comment::class.java)
                // 답변 내용 어떤 형식으로 출력?
                // 답변 없을 경우 게시판에 답변 어떻게 노출?
                if (comment?.comment != null){
                    //comment 너무 늦게 뜸
                    binding.postdetailTvCommentdetail.text = "답변 내용 : "+ comment.comment
                }
                else{
                    binding.postdetailTvCommentdetail.visibility = View.INVISIBLE

                }}
            else{
                binding.postdetailTvCommentdetail.visibility = View.INVISIBLE
            }
        }

//
//        //유저 권한 검사는 해당 액티비티에서 진행해야함
//        if (User.mode -> manager){
//            binding.postdetailBtnComment.visibility = View.ViSIBILE
//        }
//        else{
//            binding.postdetailBtnComment.visibility = View.INViSIBILE
//        }


        //key == commentId / firebase upload
        binding.postdetailBtnComment.setOnClickListener {
            postRef.child(post.postId).child("postCommentId").setValue(commentKey)
            comment.postId = post.postId
            comment.comment = binding.postdetailEtComment.text.toString()
            comment.commentId = commentKey
            commentRef.child(commentKey).setValue(comment)
            Toast.makeText(this,"Comment 등록 완료",Toast.LENGTH_SHORT).show()
        }
    }
}