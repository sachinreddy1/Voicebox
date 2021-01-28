package com.sachinreddy.feature.table.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.sachinreddy.feature.R

class SmallFAB(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    init {
        View.inflate(context, R.layout.small_fab, this)
        val fabCircle: ConstraintLayout = findViewById(R.id.fab_circle)
        val fabIcon: ImageView = findViewById(R.id.fab_icon)
        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.SmallFAB)
        fabCircle.backgroundTintList =
            attributes.getColorStateList(R.styleable.SmallFAB_fabCircleColor)
        fabCircle.visibility =
            if (attributes.getBoolean(
                    R.styleable.SmallFAB_fabEnabled,
                    false
                )
            ) View.VISIBLE else View.INVISIBLE
        fabIcon.setImageResource(
            attributes.getResourceId(
                R.styleable.SmallFAB_fabImage,
                R.drawable.ic_play
            )
        )
        fabIcon.imageTintList =
            attributes.getColorStateList(R.styleable.SmallFAB_fabImageTint)
        attributes.recycle()
    }
}