package com.miraxh.dreamer.ui.draw

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat

import com.miraxh.dreamer.R

class CanvasFragment() : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myCanvas = CanvasHelper(requireContext())
        myCanvas.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN
        return myCanvas
    }

}
