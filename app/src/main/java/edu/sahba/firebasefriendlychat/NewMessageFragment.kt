package edu.sahba.firebasefriendlychat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import edu.sahba.firebasefriendlychat.data.User
import edu.sahba.firebasefriendlychat.databinding.FragmentNewMessageBinding
import timber.log.Timber

class NewMessageFragment : MyFragment() {

    private var _binding: FragmentNewMessageBinding? = null
    private val binding get() = _binding!!

    private val adapter = GroupAdapter<GroupieViewHolder>()
    private var mRewardedAd: RewardedAd? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewMessageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchUser()

        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_newMessageFragment_to_chatListFragment)
        }

        adapter.setOnItemClickListener { item, _ ->
            val userItem = item as UsersAdapter
            val bundle = bundleOf(USER_KEY to userItem.user)
            findNavController().navigate(R.id.action_newMessageFragment_to_chatLogFragment, bundle)
            adapter.clear()
        }
    }

    private fun fetchUser() {
        val ref = database.getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    if (it.exists()) {
                        binding.emptyDataTv.visibility = View.GONE
                        binding.emptyIv.visibility = View.GONE
                        val user = (it.getValue(User::class.java))
                        if (user != null) {
                            if (user.uid == App.currentUser?.uid)
                                return@forEach
                            adapter.add(UsersAdapter(user))
                        }
                    } else {
                        binding.emptyDataTv.visibility = View.VISIBLE
                        binding.emptyIv.visibility = View.VISIBLE
                    }
                    binding.selectUserRv.adapter = adapter
                    binding.selectUserRv.addItemDecoration(
                        DividerItemDecoration(
                            context,
                            RecyclerView.VERTICAL
                        )
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.e("onCancelled")
            }

        })
    }

}