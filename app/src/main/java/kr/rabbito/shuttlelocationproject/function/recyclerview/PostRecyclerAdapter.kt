package kr.rabbito.shuttlelocationproject.function.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.rabbito.shuttlelocationproject.R
import kr.rabbito.shuttlelocationproject.data.Post

class PostRecyclerAdapter(private val context:Context):
    RecyclerView.Adapter<PostRecyclerAdapter.ViewHolder>(){

    private var postList = mutableListOf<Post>()

    fun setPostData(data:MutableList<Post>){
        postList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.post_item,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostRecyclerAdapter.ViewHolder, position: Int) {

        val post : Post = postList[position]
        holder.title.text = post.postTitle
        holder.content.text = post.postContent
        holder.date.text = post.postDate

    }

    override fun getItemCount(): Int {

        return postList.size
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.item_tv_title)
        val content : TextView = itemView.findViewById(R.id.item_tv_content)
        val date : TextView = itemView.findViewById(R.id.item_tv_date)

    }

}