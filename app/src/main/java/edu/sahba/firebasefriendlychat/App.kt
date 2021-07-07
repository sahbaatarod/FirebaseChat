package edu.sahba.firebasefriendlychat

import android.app.Application
import com.google.android.gms.ads.MobileAds
import edu.sahba.firebasefriendlychat.data.User
import timber.log.Timber

class App : Application() {

    companion object {
        var currentUser: User? = null
        var receiverId: String? = null
        const val defaultPic: String =
            "https://static-media-prod-cdn.itsre-sumo.mozilla.net/static/sumo/img/default-FFA-avatar.png"

    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}