package com.example.pslabmobile

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity

class SigninActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_signin)
    }
}