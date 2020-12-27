package com.globals.netconnect.netconnect.helper

import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

abstract class RightDrawableOnTouchListener(view: TextView) : View.OnTouchListener {
    internal var drawable: Drawable? = null
    private val fuzz = 10

    init {
        val drawables = view.compoundDrawables
        if (drawables != null && drawables.size == 4)
            this.drawable = drawables[2]
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
     */
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && drawable != null) {
            val x = event.x.toInt()
            val y = event.y.toInt()
            val bounds = drawable!!.bounds
            if (x >= v.right - bounds.width() - fuzz && x <= v.right - v.paddingRight + fuzz
                && y >= v.paddingTop - fuzz && y <= v.height - v.paddingBottom + fuzz
            ) {
                return onDrawableTouch(event)
            }
        }
        return false
    }

    abstract fun onDrawableTouch(event: MotionEvent): Boolean

}