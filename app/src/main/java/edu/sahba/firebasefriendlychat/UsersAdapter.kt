package edu.sahba.firebasefriendlychat

import android.widget.TextView
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView
import edu.sahba.firebasefriendlychat.data.User

class UsersAdapter(val user: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val img = viewHolder.itemView.findViewById<CircleImageView>(R.id.receivedMessageUserIv)
        val username = viewHolder.itemView.findViewById<TextView>(R.id.userNameTv)
        if (user.profileImgUrl.isEmpty())
            Picasso.get().load(R.drawable.outline_account_circle_black_24).into(img)
        else
            Picasso.get().load(user.profileImgUrl).into(img)
        username.text = user.fullname
    }

    override fun getLayout(): Int {
        return R.layout.item_users
    }
}