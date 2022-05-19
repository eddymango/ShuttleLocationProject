package kr.rabbito.shuttlelocationproject.function.recyclerview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kr.rabbito.shuttlelocationproject.data.Post

//datasource 캡슐화 과정
//LiveData
// -> android jetPack의 구성요소
// -> 데이터 값 자동 관찰 -> 화면 변경 기능
class Repo {
    fun getData(): LiveData<MutableList<Post>> {
        val mutableData = MutableLiveData<MutableList<Post>>()
        val database = Firebase.database
        val myRef = database.getReference("Community")

        myRef.addValueEventListener(object : ValueEventListener {

            val listData: MutableList<Post> = mutableListOf()

            override fun onDataChange(snapshot: DataSnapshot) {


                if (snapshot.exists()){

                    for (userSnapshot in snapshot.children){
                        val getData = userSnapshot.getValue(Post::class.java)
                        listData.add(getData!!)
                        mutableData.value = listData
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return mutableData
    }
}
