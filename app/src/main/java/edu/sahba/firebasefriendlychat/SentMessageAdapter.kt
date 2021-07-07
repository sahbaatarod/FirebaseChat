package edu.sahba.firebasefriendlychat

import android.widget.TextView
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView
import edu.sahba.firebasefriendlychat.data.ChatMessage
import edu.sahba.firebasefriendlychat.data.User

class SentMessageAdapter(val chat: ChatMessage, val user: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val img = viewHolder.itemView.findViewById<CircleImageView>(R.id.sentMessageUserIv)
        val msg = viewHolder.itemView.findViewById<TextView>(R.id.sentMessageText)
        Picasso.get().load(user.profileImgUrl).into(img)
        msg.text = chat.text
    }

    override fun getLayout(): Int {
        return R.layout.item_sent_message
    }
}