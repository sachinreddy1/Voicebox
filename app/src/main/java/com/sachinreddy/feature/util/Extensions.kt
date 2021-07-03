package com.sachinreddy.feature.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

infix fun Int.toward(to: Int): IntProgression {
    return if (this > to) {
        IntProgression.fromClosedRange(to, this, 1)
    } else {
        IntProgression.fromClosedRange(this, to, 1)
    }
}

fun roundClosest(value: Float): Float {
    val intValue = value.toInt()
    val decValue = value - intValue

    return if (decValue > 0.5f) {
        1 - decValue
    } else {
        decValue
    }
}

class Util {
    companion object {
        fun sleepNano(startTime: Long, interval: Long) {
            var endTime = 0L
            while (startTime + interval >= endTime) {
                endTime = System.nanoTime()
            }
        }
    }
}