package edu.sahba.firebasefriendlychat.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User(val uid: String, val fullname: String, val profileImgUrl: String) : Parcelable {
    constructor() : this("", "", "")
}