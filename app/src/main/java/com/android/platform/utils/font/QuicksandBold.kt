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
        applyCustomTextColor(context)
    }

    private fun applyCustomFont(context: Context) {
        typeface =ResourcesCompat.getFont(context, R.font.quicksand_bold)
    }
    private fun applyCustomTextColor(context: Context) {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(R.attr.headerTextColor, typedValue, true)
        @ColorInt val color: Int = typedValue.data
//        setTextColor(color)
    }
}