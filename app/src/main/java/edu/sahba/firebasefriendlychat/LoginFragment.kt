package edu.sahba.firebasefriendlychat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import edu.sahba.firebasefriendlychat.App.Companion.receiverId
import edu.sahba.firebasefriendlychat.data.User
import edu.sahba.firebasefriendlychat.databinding.FragmentLoginBinding
import timber.log.Timber

class LoginFragment : MyFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBtn.setOnClickListener {
            logIn()
        }

        binding.goToSignUpBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

    }

    private fun logIn() {
        binding.loginBtn.showLoading()
        auth.signInWithEmailAndPassword(
            binding.emailEt.text.toString(),
            binding.passwordEt.text.toString()
        )
            .addOnCompleteListener {
                binding.loginBtn.hideLoading()
                if (it.isSuccessful) {
                    receiverId = it.result?.user?.uid
                    fetchCurrentUserInfo()
                } else
                    Toast.makeText(context, "Login error!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchCurrentUserInfo() {
        val ref =
            database.getReference("/users/${auth.uid}")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                App.currentUser = snapshot.getValue(User::class.java)
                startActivity(Intent(activity, MainActivity::class.java))
                activity?.finish()
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.e("onCancelled")
            }

        })
    }


}