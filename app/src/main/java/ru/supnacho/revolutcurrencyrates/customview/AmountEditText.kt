package ru.supnacho.revolutcurrencyrates.customview

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.EditText
import ru.supnacho.revolutcurrencyrates.R

class AmountEditText  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.editTextStyle
) : EditText(context, attrs, defStyleAttr) {

    override fun onSelectionChanged(selStart: Int, selEnd: Int) = setSelection(text.length)

    override fun requestRectangleOnScreen(rectangle: Rect?): Boolean = false

}