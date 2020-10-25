package com.sachinreddy.feature.table.ui.shadow

import android.graphics.Canvas
import android.graphics.Point
import android.view.View

class UtilDragShadowBuilder(v: View) : View.DragShadowBuilder(v) {

    override fun onProvideShadowMetrics(size: Point, touch: Point) {
        val width: Int = view.width / 2
        val height: Int = view.height / 2
        size.set(width, height)
        touch.set(width / 2, height / 2)
    }

    override fun onDrawShadow(canvas: Canvas) {

    }
}