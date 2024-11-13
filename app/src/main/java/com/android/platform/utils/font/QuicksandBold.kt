package com.android.platform.utils.font

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.android.platform.R


class QuicksandBold @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        applyCustomFont(context)
        if (!isTextColorSet(attrs)) {
            applyCustomTextColor(context)
        }
    }

    private fun applyCustomFont(context: Context) {
        typeface = ResourcesCompat.getFont(context, R.font.quicksand_bold)
    }

    private fun applyCustomTextColor(context: Context) {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(R.attr.headerTextColor, typedValue, true)
        setTextColor(typedValue.data)
    }

    private fun isTextColorSet(attrs: AttributeSet?): Boolean {
        if (attrs == null) return false

        val attributes = context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.textColor))
        val hasTextColor = attributes.hasValue(0)
        attributes.recycle()
        return hasTextColor
    }
}