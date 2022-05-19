package kr.rabbito.shuttlelocationproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.rabbito.shuttlelocationproject.databinding.ActivityCommunityBinding
import kr.rabbito.shuttlelocationproject.function.recyclerview.PostRecyclerAdapter
import kr.rabbito.shuttlelocationproject.function.recyclerview.PostViewModel

class CommunityActivity : AppCompatActivity() {

    private lateinit var adapter: PostRecyclerAdapter
    private val postViewModel by lazy { ViewModelProvider(this).get(PostViewModel::class.java) }

    lateinit var binding:ActivityCommunityBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityCommunityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        adapter = PostRecyclerAdapter(this)

        val recyclerView: RecyclerView = binding.commuRvList
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        observerData()


        binding.commuBtnPost.setOnClickListener {
            val intent = Intent(this,PostActivity::class.java)
            startActivity(intent)
        }
    }





    fun observerData(){
        postViewModel.fetchData().observe(this,  {
            //adapter.setPostData(it)
            adapter.notifyDataSetChanged()
        })
    }





}
