package com.miraxh.dreamer.util

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.miraxh.dreamer.R

class DialogHelper : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Message")
                .setPositiveButton("Positive message",
                    DialogInterface.OnClickListener { dialog, id ->
                        // Positive action
                    })
                .setNegativeButton("Negative message",
                    DialogInterface.OnClickListener { dialog, id ->
                        // Negative action (dialog closure)
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}