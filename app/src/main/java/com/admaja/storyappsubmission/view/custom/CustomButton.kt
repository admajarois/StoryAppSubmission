package com.admaja.storyappsubmission.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.admaja.storyappsubmission.R
import com.google.android.material.button.MaterialButton

class CustomButton: AppCompatButton {

    private lateinit var enableButtonBackground: Drawable
    private lateinit var disableButtonBackground: Drawable
    private var txtColor: Int = 0

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = if (isEnabled) enableButtonBackground else disableButtonBackground

        setTextColor(txtColor)
        textSize = 12f
        gravity = Gravity.CENTER
    }

    private fun init() {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        enableButtonBackground = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
        disableButtonBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_disable) as Drawable
    }
}