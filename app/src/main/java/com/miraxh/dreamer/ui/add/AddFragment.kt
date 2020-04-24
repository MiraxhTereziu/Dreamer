package com.miraxh.dreamer.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.miraxh.dreamer.R
import com.miraxh.dreamer.data.dream.Dream

class AddFragment : Fragment() {

    companion object {
        fun newInstance() = AddFragment()
    }

    private lateinit var viewModel: AddViewModel
    private lateinit var saveButton: Button
    private lateinit var newDream: Dream

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_fragment, container, false)
        saveButton = view.findViewById(R.id.save_button)

        viewModel = ViewModelProvider(this).get(AddViewModel::class.java)


        saveButton.setOnClickListener {
            view.findViewById<TextView>(R.id.dream_title).onEditorAction(EditorInfo.IME_ACTION_DONE)
            view.findViewById<TextView>(R.id.dream_description).onEditorAction(EditorInfo.IME_ACTION_DONE)
            newDream = Dream(
                0,
                "22° March",
                view.findViewById<TextView>(R.id.dream_title).text.toString(),
                view.findViewById<TextView>(R.id.dream_description).text.toString()
            )
            //Log.i(LOG_TAG, "\nTitle: ${newDream.title} \nDescription: ${newDream.description}")
            viewModel.newDream(newDream)

            //momentaneamente rimando alla home
            findNavController().navigate(R.id.home_dest)
            Toast.makeText(activity, "Added ${newDream.title}", Toast.LENGTH_LONG).show()
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}


