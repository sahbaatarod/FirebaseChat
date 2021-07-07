package edu.sahba.firebasefriendlychat.data

class ChatMessage(val id: String, val text:String, val senderId: String, val receiverId:String, val timeStamp: String ) {
    constructor() : this("", "", "", "", "")
}