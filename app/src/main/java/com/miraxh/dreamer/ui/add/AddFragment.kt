package com.miraxh.dreamer.ui.add

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.miraxh.dreamer.MainActivity
import com.miraxh.dreamer.R
import com.miraxh.dreamer.data.dream.Dream
import com.miraxh.dreamer.util.DATE_CLICKED
import kotlinx.android.synthetic.main.add_fragment.*
import java.text.SimpleDateFormat
import java.util.*


class AddFragment : Fragment(), TagListAdapter.TagListener {

    companion object {
        fun newInstance() = AddFragment()
    }

    private var dateClicked: String? = null

    private lateinit var viewModel: AddViewModel

    private lateinit var saveButton: Button
    private lateinit var datePickerBtn: Button
    private lateinit var insertTagBtn: ImageButton
    private lateinit var minusBtn: FloatingActionButton

    private lateinit var newDream: Dream
    private lateinit var include: ConstraintLayout
    private lateinit var drawerButton: ImageView
    private lateinit var ratingDream: RatingBar

    private lateinit var tagsRecycleView: RecyclerView
    private lateinit var adapterTag: TagListAdapter

    private lateinit var titleToolbar: TextView
    private lateinit var insertTag: TextInputEditText
    private lateinit var title: TextView
    private lateinit var date: TextView
    private lateinit var description: TextView
    private lateinit var audioHelper: AudioHelper

    //valori data
    private var day = 0
    private var month = 0
    private var year = 0
    private var tags = mutableListOf<String>()
    var tagSet = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dateClicked = it.getString(DATE_CLICKED)
        }
        if (dateClicked != null)
            Log.i(DATE_CLICKED, dateClicked)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_fragment, container, false)

        //inizializzazione componenti
        ratingDream = view.findViewById(R.id.rating_dream)
        tagsRecycleView = view.findViewById(R.id.tags_recyclerview)
        insertTag = view.findViewById(R.id.dream_tag)
        insertTagBtn = view.findViewById(R.id.insert_tag_btn)
        include = view.findViewById<ConstraintLayout>(R.id.toolbar_add)
        minusBtn = view.findViewById(R.id.minus_action_button)
        titleToolbar = include.findViewById<TextView>(R.id.toolbar_title_normal)
        drawerButton = include.findViewById<ImageView>(R.id.drawer_icon_normal)

        //inizializzo la toolbar
        //(activity as AppCompatActivity).setSupportActionBar(toolbarNormal)

        //inizializzo i miei componenti da cui andrò a recuperare i dati
        title = view.findViewById<TextView>(R.id.dream_title)
        date = view.findViewById<TextView>(R.id.display_date)
        description = view.findViewById<TextView>(R.id.dream_description)

        //inizializzo viewModel
        viewModel = ViewModelProvider(this).get(AddViewModel::class.java)

        //inizializzo il mio bottone che fare comparire il mio date picker
        datePickerBtn = view.findViewById<Button>(R.id.date_picker)

        //inizializzo il mio bottone di salvataggio
        saveButton = view.findViewById(R.id.save_button)

        //metodo per gestire l'inseriemnto di tag
        insertTags(view)

        //salvo il sogno nel db in base ai valori inseriti
        saveDream(view)

        //classe per gestire la registrazione dell'audio
        audioHelper = AudioHelper(view,context,null)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //imposto set on click listener
        setDatePicker()
        //inizializzazione della toolbar
        initToolbar()
        //minus back btn
        initMinusBtn()
    }

    private fun saveDream(view: View) {
        //inizializzo con valori di default il mio sogno
        newDream = Dream(0, "00/00/00", "empty", "empty", listOf<String>(), 2.5F,"")

        saveButton.setOnClickListener {
            (activity as MainActivity?)?.closeKeyboard()
            //aggiungere un controllo dell'inserimento di titolo e descrizione
            val titleEmpty = title.text.isBlank()
            val descriptionEmpty = description.text.isBlank()

            //controllo presenza dati nei vari form e display di un toast nel caso non ci siano
            if (titleEmpty && descriptionEmpty) {
                Snackbar.make(view, resources.getString(R.string.insert_t_d), Snackbar.LENGTH_SHORT)
                    .show()
            } else if (titleEmpty) {
                Snackbar.make(view, resources.getString(R.string.insert_t), Snackbar.LENGTH_SHORT)
                    .show()
            } else if (descriptionEmpty) {
                Snackbar.make(view, resources.getString(R.string.insert_d), Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                //recupero i dati del nuovo dream
                newDream = Dream(
                    0,
                    date = date.text.toString(),
                    title = title.text.toString(),
                    description = description.text.toString(),
                    tags = tags,
                    rate = ratingDream.rating,
                    audioFile = audioHelper.getUriAudioFile() ?: "null"
                )
                //inserisco il nuovo sogno nel db ad aggiorno il tutto
                viewModel.newDream(newDream)
                //momentaneamente rimando alla home
                findNavController().navigate(R.id.home_dest)
                Snackbar.make(
                    view,
                    "${resources.getString(R.string.insert_t_d)} ${newDream.title}",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun insertTags(view: View) {
        adapterTag = TagListAdapter(requireContext(), tagSet.toList(), this)
        //assegno il mio adapter alla mia RecyclerView
        tagsRecycleView.adapter = adapterTag

        val emptyInputSnackbar =
            Snackbar.make(view, resources.getString(R.string.empty_tag), Snackbar.LENGTH_SHORT)
        val dublicateSnackbar =
            Snackbar.make(view, resources.getString(R.string.duplicate_tag), Snackbar.LENGTH_SHORT)

        insertTagBtn.setOnClickListener {
            //prendo in input il tag e lo aggiungo alla lista
            val input = insertTag.text.toString()
            if (input != "") {
                if (tagSet.add(input).not())
                    dublicateSnackbar.show()
            } else {
                emptyInputSnackbar.show()
            }
            //sort list
            tags = tagSet.toMutableList()
            //creo il mio adapter
            adapterTag = TagListAdapter(requireContext(), tags, this)
            //assegno il mio adapter alla mia RecyclerView
            tagsRecycleView.adapter = adapterTag
            //resetto la textfield per inserire i tag
            insertTag.setText("")
        }
    }

    private fun initMinusBtn() {
        minusBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initToolbar() {
        titleToolbar.text = resources.getString(R.string.add_toolbar_title)
        drawerButton.setOnClickListener {
            (activity as MainActivity?)?.openDrawer()
        }
    }

    private fun setDatePicker() {
        if (dateClicked != null) {
            val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
            val dateSelected = dateFormatter.parse(dateClicked)
            val yearFormatter = SimpleDateFormat("yyyy")
            val monthFormatter = SimpleDateFormat("MM")
            val dayFormatter = SimpleDateFormat("dd")

            year = yearFormatter.format(dateSelected.time).toInt()
            month = monthFormatter.format(dateSelected.time).toInt()
            day = dayFormatter.format(dateSelected.time).toInt()

        } else {
            //imposto la data di oggi come data di default
            val calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH) + 1
            year = calendar.get(Calendar.YEAR)
        }

        var finalDate = "$day/$month/$year"


        date.text = finalDate

        datePickerBtn.setOnClickListener {
            (activity as MainActivity?)?.closeKeyboard()
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    display_date.text = "$dayOfMonth/${month + 1}/$year"
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
            //datePickerDialog.window?.setBackgroundDrawableResource(colorPrimary)
            val date = Date()
            //imposta la ma data massima, ma mi sposto la data di "oggi" alla data massima
            datePickerDialog.datePicker.maxDate = date.time

            val ok = datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE)
            ok.setTextColor(Color.rgb(206, 168, 255))
            //ok.setBackgroundColor(Color.GREEN)

            val cancel: Button = datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            cancel.setTextColor(Color.rgb(206, 168, 255))
            //cancel.setBackgroundColor(Color.GREEN)
        }
    }

    override fun onTagItemListener(tag: String, position: Int) {
        //elimino tag
        tagSet.remove(tag)
        tags = tagSet.toMutableList()
        //creo il mio adapter
        adapterTag = TagListAdapter(requireContext(), tags, this)
        //assegno il mio adapter alla mia RecyclerView
        tagsRecycleView.adapter = adapterTag
    }
}



