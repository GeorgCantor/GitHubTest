package com.georgcantor.githubtest.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.georgcantor.githubtest.R
import com.georgcantor.githubtest.utils.openFragment
import com.georgcantor.githubtest.view.fragment.UsersFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openFragment(UsersFragment())
    }

}
