package com.sachinreddy.feature.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sachinreddy.feature.R
import com.sachinreddy.feature.fragment.SplashFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val splashFragment = SplashFragment()
        supportFragmentManager.beginTransaction().add(
            R.id.fragment_main_container,
            splashFragment, "splashFragment"
        ).commit()
        fragment_main_container.animate().apply {
            duration = 1000
            alpha(0f)
            scaleX(1.2f)
            scaleY(1.2f)
            withEndAction {
                alpha(1f)
                scaleX(1f)
                scaleY(1f)
                supportFragmentManager.beginTransaction().remove(splashFragment).commit()
            }
        }
    }
}
