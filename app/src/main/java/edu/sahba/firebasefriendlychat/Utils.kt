package edu.sahba.firebasefriendlychat

import android.text.TextUtils
import android.util.Patterns


fun isValidEmail(target: CharSequence): Boolean {
    return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
}
