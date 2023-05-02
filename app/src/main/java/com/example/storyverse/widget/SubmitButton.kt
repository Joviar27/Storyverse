package com.example.storyverse.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.storyverse.R

class SubmitButton : AppCompatButton {

    private lateinit var enabledBackground : Drawable
    private lateinit var disabledBackground : Drawable
    private lateinit var enabledText : String
    private lateinit var disabledText : String
    private var customTextColor : Int = 0

    constructor(context: Context) : super(context){
        init()
    }

    constructor(context: Context, attrs : AttributeSet) : super(context, attrs){
        init()
    }

    constructor(context: Context, attrs : AttributeSet, defStyleAttr : Int) : super(context, attrs, defStyleAttr){
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        background = if(isEnabled) enabledBackground else disabledBackground
        setTextColor(customTextColor)
        textSize = 12f
        gravity = Gravity.CENTER
        text = if(isEnabled) enabledText else disabledText
    }

    private fun init(){
        customTextColor = ContextCompat.getColor(context, R.color.baby_blue)
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_blue_button) as Drawable
        disabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_blue_button_disabled) as Drawable
        enabledText = resources.getString(R.string.submit)
        disabledText = resources.getString(R.string.fill_in)
    }
}