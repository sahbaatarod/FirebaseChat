package edu.sahba.firebasefriendlychat

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

const val USER_KEY = "username"

abstract class MyFragment : Fragment(), MyView {

    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var storage: FirebaseStorage

    override fun onAttach(context: Context) {
        super.onAttach(context)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
    }


    override val viewContext: Context?
        get() = context
    override val rootView: ConstraintLayout?
        get() = view as ConstraintLayout?

}

abstract class MyActivity : AppCompatActivity(), MyView {

    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var storage: FirebaseStorage

    override val viewContext: Context?
        get() = this

    override val rootView: ConstraintLayout?
        get() {
            val viewGroup = window.decorView.findViewById(android.R.id.content) as ViewGroup
            if (viewGroup !is ConstraintLayout) {
                viewGroup.children.forEach {
                    if (it is ConstraintLayout)
                        return it
                }
                throw IllegalStateException("Root view must be instance of CoordinatorLayout")
            } else
                return viewGroup
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
    }


}

interface MyView {
    val rootView: ConstraintLayout?
    val viewContext: Context?
}
