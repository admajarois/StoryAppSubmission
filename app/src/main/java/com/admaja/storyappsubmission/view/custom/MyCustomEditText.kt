package com.admaja.storyappsubmission.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.core.content.ContextCompat
import com.admaja.storyappsubmission.R
import com.google.android.material.textfield.TextInputEditText

class MyCustomEditText : TextInputEditText, TextWatcher {

    private lateinit var backgroundEditText: Drawable

    private var isValid: Boolean? = null
    set(value) {
        field = value ?: (text?.isNotEmpty() == true)
    }

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        addTextChangedListener(this)
        backgroundEditText = ContextCompat.getDrawable(context, R.drawable.bg_text_field) as Drawable
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = backgroundEditText
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        error = null
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(p0: Editable?) {
        if (text != null && text?.isNotEmpty() == true) {
            if (inputType == InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                if (length() < 6) {
                    error = context.getString(R.string.password_alert)
                    isValid = false
                } else {
                    isValid = true
                }
            } else if (inputType == InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) {
                if (!Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()) {
                    error = context.getString(R.string.invalid_email_alert)
                    isValid = false
                }else {
                    isValid = true
                }
            } else {
                isValid = true
            }
        } else {
            error = context.getString(R.string.must_filled)
            isValid = false
        }
    }
}