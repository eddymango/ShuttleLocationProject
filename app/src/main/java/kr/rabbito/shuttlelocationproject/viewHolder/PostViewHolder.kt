package kr.rabbito.shuttlelocationproject.viewHolder

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import kr.rabbito.shuttlelocationproject.data.Post
import kr.rabbito.shuttlelocationproject.databinding.PostCardBinding
import java.text.SimpleDateFormat
import java.util.*

class PostViewHolder(private val binding: PostCardBinding) : RecyclerView.ViewHolder(binding.root) {

    lateinit var postId:String

    fun bind(post: Post, context: Context) {
        binding.postcardTvTitle.text = post.postTitle

        val long_now = Date(post.postDate.toLong())
        val dateFormat = SimpleDateFormat("yyyy년 M월 d일",Locale("ko","KR"))
        val str_date = dateFormat.format(long_now)

        binding.postcardTvDate.text = str_date.toString()


        postId = post.postId
        Log.d(TAG,"PostViewHolder - bind called()")

    }
}