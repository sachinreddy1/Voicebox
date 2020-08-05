package com.sachinreddy.feature.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sachinreddy.feature.R
import com.sachinreddy.feature.injection.ApplicationComponent
import com.sachinreddy.feature.injection.DaggerApplicationComponent
import com.sachinreddy.feature.modules.ApplicationModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

class AuthActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        val component: ApplicationComponent by lazy {
            DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(application))
                .build()
        }
        component.inject(this)
    }

    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }
}
