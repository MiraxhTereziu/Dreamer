package com.miraxh.dreamer.ui.draw

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar

import com.miraxh.dreamer.R

class CanvasFragment() : Fragment() {

    private lateinit var canvas: CanvasHelper

    private lateinit var color1: Button
    private lateinit var checked1: ImageView

    private lateinit var color2: Button
    private lateinit var checked2: ImageView

    private lateinit var color3: Button
    private lateinit var checked3: ImageView

    private lateinit var color4: Button
    private lateinit var checked4: ImageView

    private lateinit var color5: Button
    private lateinit var checked5: ImageView

    private lateinit var color6: Button
    private lateinit var checked6: ImageView

    private lateinit var color7: Button
    private lateinit var checked7: ImageView

    private lateinit var thickness : SeekBar

    private lateinit var eraseBtn : TextView
    private lateinit var undoBtn: TextView
    private lateinit var resetBtn: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_canvas, container, false)

        //inizializzaione dei componenti della mia palette
        canvas = view.findViewById<CanvasHelper>(R.id.canvas)

        color1 = view.findViewById(R.id.palette_color1)
        checked1 = view.findViewById(R.id.color_checked1)

        color2 = view.findViewById(R.id.palette_color2)
        checked2 = view.findViewById(R.id.color_checked2)

        color3 = view.findViewById(R.id.palette_color3)
        checked3 = view.findViewById(R.id.color_checked3)

        color4 = view.findViewById(R.id.palette_color4)
        checked4 = view.findViewById(R.id.color_checked4)

        color5 = view.findViewById(R.id.palette_color5)
        checked5 = view.findViewById(R.id.color_checked5)

        color6 = view.findViewById(R.id.palette_color6)
        checked6 = view.findViewById(R.id.color_checked6)

        color7 = view.findViewById(R.id.palette_color7)
        checked7 = view.findViewById(R.id.color_checked7)

        thickness = view.findViewById(R.id.seekBar_thickness)

        eraseBtn = view.findViewById(R.id.erase_btn)
        undoBtn = view.findViewById(R.id.undo_btn)
        resetBtn = view.findViewById(R.id.reset_btn)

        //listeners
        color1.setOnClickListener {
            canvas.changeColor(1)
            unCheckAll()
            checked1.visibility = View.VISIBLE
        }

        color2.setOnClickListener {
            canvas.changeColor(2)
            unCheckAll()
            checked2.visibility = View.VISIBLE
        }

        color3.setOnClickListener {
            canvas.changeColor(3)
            unCheckAll()
            checked3.visibility = View.VISIBLE
        }

        color4.setOnClickListener {
            canvas.changeColor(4)
            unCheckAll()
            checked4.visibility = View.VISIBLE
        }

        color5.setOnClickListener {
            canvas.changeColor(5)
            unCheckAll()
            checked5.visibility = View.VISIBLE
        }

        color6.setOnClickListener {
            canvas.changeColor(6)
            unCheckAll()
            checked6.visibility = View.VISIBLE
        }

        color7.setOnClickListener {
            canvas.changeColor(7)
            unCheckAll()
            checked7.visibility = View.VISIBLE
        }

        thickness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                canvas.changeThickness(i)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
            }
        })

        eraseBtn.setOnClickListener {
            canvas.changeColor(0)
            unCheckAll()
            eraseBtn.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_palette_btn,null)
        }

        undoBtn.setOnClickListener {
            canvas.undo()
        }

        resetBtn.setOnClickListener {
            canvas.resetCanvas()
            Snackbar.make(
                view,
                "Canvas cleaned",
                Snackbar.LENGTH_SHORT
            ).show()
        }

        return view
    }

    private fun unCheckAll(){
        checked1.visibility = View.INVISIBLE
        checked2.visibility = View.INVISIBLE
        checked3.visibility = View.INVISIBLE
        checked4.visibility = View.INVISIBLE
        checked5.visibility = View.INVISIBLE
        checked6.visibility = View.INVISIBLE
        checked7.visibility = View.INVISIBLE
        eraseBtn.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_palette_btn_disable,null)
    }
}
