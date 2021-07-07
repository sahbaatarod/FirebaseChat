package edu.sahba.firebasefriendlychat

import android.os.Bundle
import edu.sahba.firebasefriendlychat.databinding.ActivityMainBinding

class MainActivity : MyActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}