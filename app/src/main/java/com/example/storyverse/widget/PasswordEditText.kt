package com.example.storyverse.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.storyverse.R

class PasswordEditText : AppCompatEditText {

    private lateinit var normalBackground : Drawable
    private lateinit var errorBackground : Drawable
    private var txtColor : Int = 0
    private var isError : Boolean = false

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
        setTextColor(txtColor)
        background = if(isError) errorBackground else normalBackground

        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                when{
                    s.toString().isEmpty() -> {
                        isError = true
                        error = resources.getString(R.string.pass_empty)
                    }
                    s.toString().count()<8 -> {
                        isError = true
                        error = resources.getString(R.string.pass_error)
                    }
                    else -> {
                        isError = false
                        error = null
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //nothing
            }
        })
    }

    private fun init(){
        normalBackground = ContextCompat.getDrawable(context, R.drawable.bg_edit_text) as Drawable
        errorBackground = ContextCompat.getDrawable(context, R.drawable.bg_edit_text_error) as Drawable
        txtColor = ContextCompat.getColor(context, android.R.color.black)
    }
}