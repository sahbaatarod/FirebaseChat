package edu.sahba.firebasefriendlychat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import edu.sahba.firebasefriendlychat.data.ChatMessage
import edu.sahba.firebasefriendlychat.data.User
import edu.sahba.firebasefriendlychat.databinding.FragmentChatLogBinding
import timber.log.Timber
import java.util.*

class ChatLogFragment : MyFragment() {

    private var _binding: FragmentChatLogBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: User
    private lateinit var senderId: String
    private lateinit var receiverId: String
    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        user = arguments?.getParcelable(USER_KEY)!!
        senderId = auth.uid!!
        receiverId = user.uid
        _binding = FragmentChatLogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.userNameTv.text = user.fullname
        binding.chatMessagesRv.adapter = adapter
        listenForMessages()

        binding.sendMsgBtn.setOnClickListener {
            if (binding.msgEt.length() != 0)
                sendMessage()
            binding.msgEt.text?.clear()
            binding.chatMessagesRv.smoothScrollToPosition(adapter.itemCount + 1)
        }

        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_chatLogFragment_to_chatListFragment)
        }

    }

    private fun listenForMessages() {
        val ref = database.getReference("/userMessages/$receiverId/$senderId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                if (chatMessage != null) {
                    if (chatMessage.receiverId == auth.uid) {
                        adapter.add(ReceivedMessageAdapter(chatMessage, user))
                        binding.chatMessagesRv.smoothScrollToPosition(adapter.itemCount + 1)
                    } else
                        adapter.add(SentMessageAdapter(chatMessage, App.currentUser!!))
                }

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Timber.e("onChildChanged")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Timber.e("${snapshot.getValue(ChatMessage::class.java)}")
                adapter.notifyDataSetChanged()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Timber.e("onChildMoved")
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.e("onCancelled")
            }

        })
    }

    private fun sendMessage() {
        val msg = binding.msgEt.text.toString()

        val receiverReference = database.getReference("/userMessages/$receiverId/$senderId").push()
        val senderReference = database.getReference("/userMessages/$senderId/$receiverId").push()
        val receiverLatestMessage =
            database.getReference("/latestMessages/$receiverId/$senderId")
        val senderLatestMessageSender =
            database.getReference("latestMessages/$senderId/$receiverId")

        val chatMessage =
            ChatMessage(
                receiverReference.key!!,
                msg,
                senderId,
                receiverId,
                Calendar.getInstance().time.toString()
            )
        receiverReference.setValue(chatMessage)
        senderReference.setValue(chatMessage)
        receiverLatestMessage.setValue(chatMessage)
        senderLatestMessageSender.setValue(chatMessage)


    }


}