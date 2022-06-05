package kr.rabbito.shuttlelocationproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kr.rabbito.shuttlelocationproject.data.Comment
import kr.rabbito.shuttlelocationproject.data.CommentDialog
import kr.rabbito.shuttlelocationproject.data.DeleteDialog
import kr.rabbito.shuttlelocationproject.data.Post
import kr.rabbito.shuttlelocationproject.databinding.ActivityPostDetailBinding
import kr.rabbito.shuttlelocationproject.function.hashSHA256
import java.text.SimpleDateFormat
import java.util.*

//< PostDetailActivity - xml >
//댓글 x
//-> 아예 안뜨게
//-> 관리자한테는 등록 버튼
//댓글 o
//-> 둘 다 박스 뜨고
//-> 관리자한테는 삭제 버튼
//<순서> 댓글등록또는삭제 변경 삭제

class PostDetailActivity : AppCompatActivity() {
    private var mBinding: ActivityPostDetailBinding? = null
    private val binding get() = mBinding!!
    val TAG: String = "TAG"
    //private var time = SimpleDateFormat("yyyy년 MM월 dd일").format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityPostDetailBinding.inflate(layoutInflater)

        val shared = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        //저장되어 있는 UserMode 값 getString / default Value : User
        val loginMode = shared.getString("UserMode", "User")

        //UserMode : Manager
        if (loginMode == "Manager") {
            binding.postdetailBtnComment.visibility = View.VISIBLE

        }
        //UserMode : Manager
        else{
            binding.postdetailBtnComment.visibility = View.GONE

        }

        val bundle = intent.extras
        val post = bundle!!.getParcelable<Post>("selectedPost")!!

        Firebase.database.getReference("Community/Comment").child(post.postCommentId).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val comment = task.result.getValue(Comment::class.java)
                    if (comment?.comment !=null && comment.comment !="" ) {
                        binding.postdetailTvCommentdetail.text = comment.comment
                        binding.postdetailTvCommentempty.visibility = View.INVISIBLE

                        if (loginMode == "Manager"){
                            binding.postdetailBtnComment.visibility = View.GONE
                            binding.postdetailBtnCommentdelete.visibility = View.VISIBLE

                        }
                        else{
                            binding.postdetailBtnComment.visibility = View.GONE
                            binding.postdetailBtnCommentdelete.visibility = View.GONE

                        }
                    }
                }
            }

        setContentView(binding.root)
        overridePendingTransition(0, 0)
        Log.d(TAG, "PostDetailActivity called()")

        //intent에 담긴 bundle -> post에 풀기

        val comment = Comment()

        //Toast.makeText(this,"ClcikListener 실행 완료 ",Toast.LENGTH_SHORT).show()
        val commentRef = FirebaseDatabase.getInstance().getReference("Community/Comment")
        val postRef = FirebaseDatabase.getInstance().getReference("Community/Post")
        //key == commentId
        val commentKey = commentRef.push().key!!

        // post_detail.xml
        binding.postdetailTvTitle.text = post.postTitle
        binding.postdetailTvContent.text = post.postContent
        val long_now = Date(post.postDate.toLong())
        val dateFormat = SimpleDateFormat("yyyy년 M월 d일",Locale("ko","KR"))
        val str_date = dateFormat.format(long_now)
        binding.postdetailTvDate.text = str_date.toString()
        //날짜 받아와서 변환하기

        //comment 없을 경우 NullPointerException 예외 처리?

        // 게시물 수정하기 삭제하기 ->
        binding.postdetailBtnDelete.setOnClickListener {
            val dialog = DeleteDialog(this@PostDetailActivity)
            dialog.showDialog()
            dialog.setOnClickListner(object : DeleteDialog.ButtonClickListener {
                override fun onClicked(text: String) {
                    val inputPassword = text.hashSHA256()
                    //Toast.makeText(this@PostDetailActivity, inputPassword, Toast.LENGTH_SHORT).show()
                    //입력한 비밀번호와 post에 저장되어 있는 게시물 비밀번호 같을 경우
                    if (inputPassword == post.postPassword) {
                        //post 삭제
                        postRef.child(post.postId).removeValue()
                        //comment 삭제
                        commentRef.child(post.postCommentId).removeValue()
                        Toast.makeText(this@PostDetailActivity, "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    } else { // 게시물 비밀번호 다를 경우
                        Toast.makeText(this@PostDetailActivity, "잘못된 비밀번호입니다..", Toast.LENGTH_SHORT).show()
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
                        intent.putExtra("PostPassword", text)
                        //post 삭제
                        finish()
                        startActivity(intent)

                    } else { // 게시물 비밀번호 다를 경우
                        Toast.makeText(this@PostDetailActivity, "잘못된 비밀번호입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }


        //key == commentId / firebase upload
        binding.postdetailBtnComment.setOnClickListener {

            val dialog = CommentDialog(this@PostDetailActivity)

            dialog.showDialog()
            dialog.setOnClickListner(object : CommentDialog.ButtonClickListener {
                override fun onClicked(text: String) {
                    if (text == "") {
                        Toast.makeText(this@PostDetailActivity, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    } else {
                        postRef.child(post.postId).child("postCommentId").setValue(commentKey)
                        comment.postId = post.postId
                        comment.comment = text
                        comment.commentId = commentKey
                        commentRef.child(commentKey).setValue(comment)
                        post.postCommentId = comment.commentId

                        val tintent = Intent(this@PostDetailActivity, PostDetailActivity::class.java)

                        val tmpbundle = Bundle()
                        tmpbundle.putParcelable("selectedPost",post)
                        tintent.putExtras(tmpbundle)
                        Toast.makeText(this@PostDetailActivity, "답변이 등록되었습니다.", Toast.LENGTH_SHORT)
                            .show()


                        finish()
                        startActivity(tintent)

                    }
                }
            })

        }
        binding.postdetailBtnCommentdelete.setOnClickListener {
            // 체크 박스
            val dialogView = View.inflate(this, R.layout.check_dialog, null)
            val check = AlertDialog.Builder(this)
            val dlg = check.create()
            val check_ok_btn = dialogView.findViewById<TextView>(R.id.checkdialog_btn_ok)
            val check_cancel_btn =
                dialogView.findViewById<TextView>(R.id.checkdialog_btn_cancel)
            val check_tv = dialogView.findViewById<TextView>(R.id.checkdialog_tv_title)
            check_tv.text = "답변을 삭제하시겠습니까?"
            check_cancel_btn.setOnClickListener { dlg.dismiss() }
            // 확인 버튼
            check_ok_btn.setOnClickListener {
                if (binding.postdetailTvCommentdetail.text.toString() != ""){
                    commentRef.child(post.postCommentId).removeValue()
                    Toast.makeText(this, "답변이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    val tintent = Intent(this@PostDetailActivity, PostDetailActivity::class.java)

                    val tmpbundle = Bundle()
                    tmpbundle.putParcelable("selectedPost",post)
                    tintent.putExtras(tmpbundle)

                    finish()
                    startActivity(tintent)
                } else {
                    Toast.makeText(this, "삭제 중 오류가 발생했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
                }
            }
            dlg.setView(dialogView)
            dlg.show()
        }

        binding.postdetailBtnBack.setOnClickListener {
            finish()
        }
    }

    override fun finish() {
        super.finish()
        binding.postdetailTvCommentempty.visibility = View.VISIBLE
        binding.postdetailTvCommentempty.visibility = View.VISIBLE

        overridePendingTransition(0, 0)
    }
}