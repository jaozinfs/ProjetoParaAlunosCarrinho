package com.example.developersapp.presentation.extensions

import android.view.View
import androidx.core.view.isVisible

fun View.animateGone() {
    if (isVisible)
        animate().alpha(0.3f).withEndAction {
            isVisible = false
            alpha = 1f
        }.setDuration(100).start()
}