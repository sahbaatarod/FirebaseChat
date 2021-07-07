package edu.sahba.firebasefriendlychat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.sahba.firebasefriendlychat.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}