package kr.rabbito.shuttlelocationproject.function.recyclerview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.core.Repo
import kr.rabbito.shuttlelocationproject.data.Post

// ViewModel -> repo에 있는 데이터 관찰 -> 변경 -> mutableData의 값 변경 역할
class PostViewModel : ViewModel(){

    private val repo = Repo()
    fun fetchData(): LiveData<MutableList<Post>> {
        val mutableData = MutableLiveData<MutableList<Post>>()
        repo.getData().observeForever {
            mutableData.value = it
        }
        return mutableData

    }
}