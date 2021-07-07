package edu.sahba.firebasefriendlychat

import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView
import edu.sahba.firebasefriendlychat.data.ChatMessage
import edu.sahba.firebasefriendlychat.data.User
import timber.log.Timber

class LatestMessageAdapter(val chatMessage: ChatMessage) : Item<GroupieViewHolder>() {
    var chatPartnerUser: User? = null
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val img = viewHolder.itemView.findViewById<CircleImageView>(R.id.userImgChatList)
        val username = viewHolder.itemView.findViewById<TextView>(R.id.userNameTvChatList)
        val lastMsg = viewHolder.itemView.findViewById<TextView>(R.id.lastMsgTv)
        val date = viewHolder.itemView.findViewById<TextView>(R.id.dateTv)


        val chatPartnerId = if (chatMessage.receiverId == FirebaseAuth.getInstance().uid) {
            chatMessage.senderId
        } else
            chatMessage.receiverId

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatPartnerUser = snapshot.getValue(User::class.java)
                username.text = chatPartnerUser?.fullname
                if (chatPartnerUser?.profileImgUrl!!.isEmpty())
                    Picasso.get().load(R.drawable.outline_account_circle_black_24).into(img)
                else
                    Picasso.get().load(chatPartnerUser?.profileImgUrl).into(img)
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.e("onCancelled")
            }

        })

        lastMsg.text = chatMessage.text
    }

    override fun getLayout(): Int {
        return R.layout.item_chat_list
    }
}