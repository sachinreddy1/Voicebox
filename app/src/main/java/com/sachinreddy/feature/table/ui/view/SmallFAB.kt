package com.sachinreddy.feature.table.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.sachinreddy.feature.R

class SmallFAB(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    init {
        View.inflate(context, R.layout.small_fab, this)
        val fabCircle: ConstraintLayout = findViewById(R.id.fab_circle)
        val fabIcon: ImageView = findViewById(R.id.fab_icon)
        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.SmallFAB)

        val backgroundColor = attributes.getColorStateList(R.styleable.SmallFAB_fabCircleColor)
        fabCircle.backgroundTintList = backgroundColor

        attributes.getBoolean(
            R.styleable.SmallFAB_fabEnabled,
            false
        ).apply {
            isEnabled = this

            fabCircle.backgroundTintList =
                if (this) backgroundColor else context.getColorStateList(R.color.selection_color)
            fabCircle.children.forEach { child -> child.isEnabled = this }
        }

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

    fun setFabEnabled(fabEnabled: Boolean) {
        val fabCircle: ConstraintLayout = findViewById(R.id.fab_circle)

        isEnabled = fabEnabled
        fabCircle.backgroundTintList =
            if (fabEnabled) context.getColorStateList(R.color.whitesmoke) else context.getColorStateList(
                R.color.selection_color
            )
        fabCircle.children.forEach { child -> child.isEnabled = fabEnabled }
    }
}