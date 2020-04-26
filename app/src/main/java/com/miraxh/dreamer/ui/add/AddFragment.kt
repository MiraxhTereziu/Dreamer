package com.miraxh.dreamer.ui.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.miraxh.dreamer.R
import com.miraxh.dreamer.data.dream.Dream
import kotlinx.android.synthetic.main.add_fragment.*
import java.util.*


class AddFragment : Fragment() {

    companion object {
        fun newInstance() = AddFragment()
    }

    private lateinit var viewModel: AddViewModel
    private lateinit var saveButton: Button
    private lateinit var newDream: Dream
    private lateinit var datepickerBtn: Button

    private lateinit var title: TextView
    private lateinit var date: TextView
    private lateinit var description: TextView

    //valori data
    private var day = 0
    private var month = 0
    private var year = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_fragment, container, false)

        //inizializzo i miei componenti da cui andrò a recuperare i dati
        title = view.findViewById<TextView>(R.id.dream_title)
        date = view.findViewById<TextView>(R.id.display_date)
        description = view.findViewById<TextView>(R.id.dream_description)

        //inizializzo viewModel
        viewModel = ViewModelProvider(this).get(AddViewModel::class.java)

        //imposto la data di oggi come data di default
        val calendar = Calendar.getInstance()
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)

        val finalDate = "$day/${month + 1}/$year"
        date.text = finalDate

        //inizializzo il mio bottone che fare comparire il mio date picker
        datepickerBtn = view.findViewById<Button>(R.id.date_picker)

        //imposto set on click listener
        datepickerBtn.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    display_date.text = "$dayOfMonth/${month + 1}/$year"
                },
                year,
                month,
                day
            ).show()
        }

        //inizializzo il mio bottone di salvataggio
        saveButton = view.findViewById(R.id.save_button)

        //inizializzo con valori di default il mio sogno
        newDream = Dream(0, "00/00/00", "empty", "empty")

        saveButton.setOnClickListener {
            //aggiungere un controllo dell'inserimento di titolo e descrizione
            val titleEmpty = title.text.isBlank()
            val descriptionEmpty = description.text.isBlank()

            //controllo presenza dati nei vari form e display di un toast nel caso non ci siano
            if (titleEmpty && descriptionEmpty) {
                Snackbar.make(view, "Insert title and description", Snackbar.LENGTH_SHORT).show()
            } else if (titleEmpty) {
                Snackbar.make(view, "Insert title", Snackbar.LENGTH_SHORT).show()
            } else if (descriptionEmpty) {
                Snackbar.make(view, "Insert description", Snackbar.LENGTH_SHORT).show()
            } else {
                //aggiungere metodo per chiudere la tastiera ad inserimento completato

                //recupero i dati del nuovo dream
                newDream = Dream(
                    0,
                    date = date.text.toString(),
                    title = title.text.toString(),
                    description = description.text.toString()
                )
                //inserisco il nuovo sogno nel db ad aggiorno il tutto
                viewModel.newDream(newDream)
                //momentaneamente rimando alla home
                findNavController().navigate(R.id.home_dest)
                Snackbar.make(view, "Added ${newDream.title}", Snackbar.LENGTH_SHORT).show()
            }
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}


