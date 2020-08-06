package com.sachinreddy.feature.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sachinreddy.feature.R
import com.sachinreddy.feature.injection.appComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

class AppActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
        appComponent.inject(this)
    }

    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }
}
