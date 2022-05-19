package kr.rabbito.shuttlelocationproject.data

import android.os.Parcel
import android.os.Parcelable

class Post(
    var postId: String ="",
    var postTitle: String = "",
    var postContent: String="",
    var postPassword: String="",
    var postDate: String=""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(postId)
        parcel.writeString(postTitle)
        parcel.writeString(postContent)
        parcel.writeString(postPassword)
        parcel.writeString(postDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }


}