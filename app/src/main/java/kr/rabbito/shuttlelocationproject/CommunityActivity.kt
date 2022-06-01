package kr.rabbito.shuttlelocationproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kr.rabbito.shuttlelocationproject.adapter.PostAdapter
import kr.rabbito.shuttlelocationproject.data.Post
import kr.rabbito.shuttlelocationproject.databinding.ActivityCommunityBinding

class CommunityActivity : AppCompatActivity() {

    val TAG :String = "TAG"
    private var mBinding: ActivityCommunityBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCommunityBinding.inflate(layoutInflater)

        setContentView(binding.root)
        overridePendingTransition(0, 0)
        //Log.d(TAG,"CommunityActivity called()")
        //postList -> RecyclerView에 나올 post 항목들 저장하는 공간
        val postList = mutableListOf<Post>()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true

        binding.communityRvList.layoutManager = layoutManager
        binding.communityRvList.adapter = PostAdapter(this, postList)
        Log.d(TAG,"CommunityActivity Adatper called()")

        binding.communityBtnPost.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            startActivity(intent)
            finish()

        }
        //Firebase 변화 감지
        FirebaseDatabase.getInstance().getReference("Community/Post")
            .orderByChild("postDate").addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    snapshot.let { snapshot ->
                        val post = snapshot.getValue(Post::class.java)
                        post?.let {
                            if (previousChildName == null) {
                                postList.add(it)
                                binding.communityRvList.adapter?.notifyItemInserted(postList.size - 1)
                            } else {
                                val prevIndex =
                                    postList.map { it.postId }.indexOf(previousChildName)
                                postList.add(prevIndex + 1, post)
                                binding.communityRvList.adapter?.notifyItemInserted(prevIndex + 1)
                            }
                        }
                    }
                }

                override fun onChildChanged(
                    snapshot: DataSnapshot,
                    previousChildName: String?
                ) {
                    snapshot.let { snapshot ->
                        val post = snapshot.getValue(Post::class.java)
                        post?.let {
                            val prevIndex =
                                postList.map { it.postId }.indexOf(previousChildName)
                            postList[prevIndex + 1] = post
                            binding.communityRvList.adapter?.notifyItemChanged(prevIndex + 1)
                        }
                    }
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    snapshot.let {
                        val post = snapshot.getValue(Post::class.java)
                        post?.let { post ->
                            val existIndex = postList.map{it.postId}.indexOf(post.postId)
                            postList.removeAt(existIndex)
                            binding.communityRvList.adapter?.notifyItemRemoved(existIndex)
                            if (previousChildName == null) {
                                postList.add(post)
                                binding.communityRvList.adapter?.notifyItemChanged(postList.size - 1)
                            } else {
                                val prevIndex =
                                    postList.map { it.postId }.indexOf(previousChildName)
                                postList.add(prevIndex + 1, post)
                                binding.communityRvList.adapter?.notifyItemChanged(prevIndex + 1)
                            }
                        }
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    snapshot.let {
                        val post = snapshot.getValue(Post::class.java)
                        post?.let { post ->
                            val existIndex = postList.map{it.postId}.indexOf(post.postId)
                            postList.removeAt(existIndex)
                            binding.communityRvList.adapter?.notifyItemRemoved(existIndex)
                            binding.communityRvList.adapter?.notifyItemRangeChanged(
                                existIndex,
                                postList.size
                            )
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    error.toException().printStackTrace()
                }
            })
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}