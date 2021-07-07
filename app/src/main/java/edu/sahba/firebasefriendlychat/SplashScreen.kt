package edu.sahba.firebasefriendlychat

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import edu.sahba.firebasefriendlychat.App.Companion.receiverId
import edu.sahba.firebasefriendlychat.data.User
import timber.log.Timber

class SplashScreen : MyActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            if (auth.uid != null) {
                receiverId = auth.uid
                fetchCurrentUserInfo()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, AuthenticationActivity::class.java))
                finish()
            }
        }, 3000)

    }

    private fun fetchCurrentUserInfo() {
        val ref =
            database.getReference("/users/${auth.uid}")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                App.currentUser = snapshot.getValue(User::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.e("onCancelled")
            }

        })
    }


}