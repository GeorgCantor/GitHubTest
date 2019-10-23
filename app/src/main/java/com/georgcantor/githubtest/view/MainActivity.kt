package com.georgcantor.githubtest.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.georgcantor.githubtest.R
import com.georgcantor.githubtest.utils.DisposableManager
import com.georgcantor.githubtest.utils.openFragment
import com.georgcantor.githubtest.view.fragment.SignInFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.georgcantor.githubtest.view.fragment.UsersFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (googleSignInAccount != null) {
            openFragment(UsersFragment())
        } else {
            openFragment(SignInFragment())
        }
    }

    override fun onDestroy() {
        DisposableManager.dispose()
        super.onDestroy()
    }

}
