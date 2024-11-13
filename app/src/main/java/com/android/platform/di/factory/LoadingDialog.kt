package com.android.platform.di.factory

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import com.android.platform.R

class LoadingDialog(context: Context) {

    private var dialog: Dialog = Dialog(context)

    init {
        reInit()
    }
    private fun reInit(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.view_loading)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

    }

    fun changeContext(ncontext: Context){
        if (!dialog.isShowing) {
            dialog.dismiss()
        }
        dialog = Dialog(ncontext)
        reInit();
    }

    fun show() {
        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    fun dismiss() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }
}
