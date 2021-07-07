package edu.sahba.firebasefriendlychat

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import edu.sahba.firebasefriendlychat.App.Companion.currentUser
import edu.sahba.firebasefriendlychat.App.Companion.defaultPic
import edu.sahba.firebasefriendlychat.App.Companion.receiverId
import edu.sahba.firebasefriendlychat.data.User
import edu.sahba.firebasefriendlychat.databinding.FragmentSignUpBinding
import timber.log.Timber
import java.util.*

class SignUpFragment : MyFragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpBtn.setOnClickListener {
            binding.signUpBtn.showLoading()
            if (checkUserInput()) {
                auth.createUserWithEmailAndPassword(
                    binding.emailEt.text.toString(),
                    binding.passwordEt.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        uploadExtraInfo()
                        receiverId = it.result?.user?.uid
                    } else Log.e("TAG", "onViewCreated: User Creation error")
                }
                    .addOnFailureListener {
                        Timber.e(it.message)
                        binding.signUpBtn.hideLoading()
                    }
            }
        }

        binding.profileImgSignUp.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        binding.goToLoginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
    }

    private fun uploadExtraInfo() {
        if (selectedPhotoUri == null) {
            saveUserData(defaultPic)
            return
        } else {
            val filename = UUID.randomUUID().toString()
            val ref = storage.getReference("/images/$filename")
            ref.putFile(selectedPhotoUri!!)
                .addOnCompleteListener {
                    ref.downloadUrl.addOnCompleteListener {
                        Log.e("TAG", "uploadExtraInfo: ${it.result.toString()}")
                        saveUserData(it.result.toString())
                    }
                }
                .addOnFailureListener {
                    Timber.e(it.message)
                }
        }
    }

    private fun saveUserData(uri: String) {
        val uid = auth.uid ?: ""
        val ref = database.getReference("/users/$uid")
        val user = User(
            uid,
            "${binding.fNameEt.text.toString()} ${binding.lNameEt.text.toString()}",
            uri
        )
        ref.setValue(user)
            .addOnSuccessListener {
                currentUser = user
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity?.startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
        binding.signUpBtn.hideLoading()
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(
                requireContext().contentResolver,
                selectedPhotoUri
            )

            binding.profileImgSignUp.setImageBitmap(bitmap)
        }
    }

    private fun checkUserInput(): Boolean {
        if (binding.fNameEt.text == null) {
            binding.fNameEt.error = "Please enter your name!"
            return false
        } else binding.fNameEt.error = null
        if (binding.lNameEt.text == null) {
            binding.lNameEt.error = "Please enter your lastname!"
            return false
        } else binding.lNameEt.error = null
        if (binding.emailEt.text == null) {
            binding.emailEt.error = "Please enter your email"
            return false
        } else binding.emailEt.error = null
        if (binding.passwordEt.length() < 6) {
            binding.passwordEt.error = "Password must be 6 charachters or more"
            return false
        } else binding.passwordEt.error = null

        return true
    }

}