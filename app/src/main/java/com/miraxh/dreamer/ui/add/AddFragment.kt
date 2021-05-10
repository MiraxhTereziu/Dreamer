package com.miraxh.dreamer.ui.add

import android.app.Activity
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.miraxh.dreamer.MainActivity
import com.miraxh.dreamer.R
import com.miraxh.dreamer.data.dream.Dream
import com.miraxh.dreamer.ui.add.audio.AudioHelper
import com.miraxh.dreamer.ui.add.audio.AudioListAdapter
import com.miraxh.dreamer.ui.add.images.ImageListAdapter
import com.miraxh.dreamer.ui.add.tag.TagListAdapter
import com.miraxh.dreamer.util.*
import kotlinx.android.synthetic.main.fragment_add.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddFragment : Fragment(), TagListAdapter.TagListener, AudioListAdapter.AudioListener,
    ImageListAdapter.ImageListener {

    private lateinit var viewModel: AddViewModel

    private lateinit var auth: FirebaseAuth

    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var datePickerBtn: Button
    private lateinit var insertTagBtn: ImageButton
    private lateinit var cancelBtn: ImageView
    private lateinit var audioBtn: ImageButton

    private lateinit var newDream: Dream
    private lateinit var include: ConstraintLayout
    private lateinit var drawerButton: ImageView
    private lateinit var ratingDream: RatingBar

    private lateinit var tagsRecycleView: RecyclerView
    private lateinit var adapterTag: TagListAdapter
    private lateinit var audioRecycleView: RecyclerView
    private lateinit var adapterAudio: AudioListAdapter
    private lateinit var imageRecycleView: RecyclerView
    private lateinit var adapterImage: ImageListAdapter

    private lateinit var titleToolbar: TextView
    private lateinit var insertTag: TextInputEditText
    private lateinit var title: TextView
    private lateinit var date: TextView
    private lateinit var description: TextView
    private lateinit var addDraw: TextView
    private lateinit var addImage: TextView
    private lateinit var recordingLabel: TextView

    private lateinit var audioHelper: AudioHelper

    //varibili gestione audio
    private var listAudio = mutableListOf<String>()
    private var recordingState = 0
    private lateinit var chronometer: Chronometer

    //valori data
    private var day = 0
    private var month = 0
    private var year = 0

    //variabili per la gestione dei tag
    private var tags = mutableListOf<String>()
    private var tagSet = mutableSetOf<String>()

    //variabili per la gestione del date picker
    private var dateClicked: String? = null
    private var datePicked: String? = null

    //variabili per la gestione immagini
    private var imageList = mutableListOf<String>()

    private var restoreDream: Dream? = null
    private var editable = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dateClicked = it.getString(DATE_CLICKED)
            val tmpSerial = it.getSerializable(RESTORE_DREAM)
            if (tmpSerial != null)
                restoreDream = tmpSerial as Dream
            editable = it.getBoolean(EDITABLE)
        }
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        setDialogBack(true)

        //inizializzazione dei componenti
        initComponents(view)

        //inizializzo la toolbar
        //(activity as AppCompatActivity).setSupportActionBar(toolbarNormal)

        //inizializzo viewModel
        viewModel = ViewModelProvider(this).get(AddViewModel::class.java)

        //inizializzo il mio bottone che fare comparire il mio date picker
        datePickerBtn = view.findViewById(R.id.date_picker)

        //inizializzo il mio bottone di salvataggio
        floatingActionButton = view.findViewById(R.id.save_button)

        //metodo per gestire l'inseriemnto di tag
        initTags(view)

        //salvo il sogno nel db in base ai valori inseriti
        initFloatAction(view)

        //classe per gestire la registrazione dell'audio
        initAudioFile(view)


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //imposto set on click listener
        setDatePicker()

        //inizializzazione della toolbar
        initToolbar()

        //minus back btn
        initBackButton()

        //gestione inserimento disegno ed immagine
        initAddDrawAndImage()

        //restore dei dati in caso ce ne fossero
        if (restoreDream != null)
            restoreDreamState(editable)
    }

    private fun initComponents(view: View) {
        //inizializzazione componenti
        imageRecycleView = view.findViewById(R.id.images_recyclerview)
        chronometer = view.findViewById(R.id.audio_chronometer)
        recordingLabel = view.findViewById(R.id.recording_label)
        audioBtn = view.findViewById(R.id.recording_btn)
        addDraw = view.findViewById(R.id.add_draw)
        addImage = view.findViewById(R.id.add_image)
        ratingDream = view.findViewById(R.id.rating_dream)
        tagsRecycleView = view.findViewById(R.id.tags_recyclerview)
        audioRecycleView = view.findViewById(R.id.audio_recyclerview)
        insertTag = view.findViewById(R.id.dream_tag)
        insertTagBtn = view.findViewById(R.id.insert_tag_btn)
        include = view.findViewById(R.id.toolbar_add)
        cancelBtn = include.findViewById(R.id.back)
        titleToolbar = include.findViewById(R.id.toolbar_title)
        drawerButton = include.findViewById(R.id.drawer_icon_normal)
        title = view.findViewById(R.id.dream_title)
        description = view.findViewById(R.id.dream_description)
    }

    private fun setDialogBack(mode: Boolean) {
        if (editable) {
            val callback: OnBackPressedCallback =
                object : OnBackPressedCallback(true /* enabled by default */) {
                    override fun handleOnBackPressed() {
                        if (Dream.showSaveDialog(requireContext(), findNavController()).not()) {
                            if (mode) {
                                Log.i("update_dream", "deleteAll")
                                deleteALl()
                            }
                        }
                    }
                }
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        }
    }

    private fun initAddDrawAndImage() {
        addDraw.setOnClickListener {
            (activity as MainActivity?)?.closeKeyboard()
            val dreamBundle = Bundle()
            dreamBundle.putSerializable(RESTORE_DREAM, getData())
            findNavController().navigate(R.id.canvasFragment, dreamBundle)
        }

        addImage.setOnClickListener {
            pickImageFromGallery()
        }
    }

    private fun deleteALl() {
        if (listAudio.isEmpty().not()) {
            listAudio.forEach {
                val tmpFile = File(Dream.getBasePath(requireContext(), FOLDER_AUDIO), it)
                tmpFile.delete()
            }
        }
        if (imageList.isEmpty().not()) {
            imageList.forEach {
                val tmpFile = File(Dream.getBasePath(requireContext(), FOLDER_IMAGE), it)
                tmpFile.delete()
            }
        }
    }

    private fun restoreDreamState(state: Boolean) {
        //ripristiono dati del sogno

        //datepicker
        datePickerBtn.text = restoreDream?.date

        //titolo
        title.text = restoreDream?.title

        //descrizione
        description.text = restoreDream?.description

        //tags
        tagSet = restoreDream?.tags?.toMutableSet() ?: mutableSetOf()
        tags = restoreDream?.tags ?: mutableListOf()

        //rating
        ratingDream.rating = restoreDream?.rate ?: 2.5F

        //audio
        listAudio = restoreDream?.audios ?: mutableListOf()

        //image rw
        imageList = restoreDream?.images ?: mutableListOf()
        adapterImage = ImageListAdapter(
            requireContext(),
            imageList
        )

        images_recyclerview.adapter = adapterImage

        //flating button
        if (editable.not())
            floatingActionButton.setImageResource(R.drawable.ic_edit)

        changeStateFields(state)

    }

    private fun initAudioFile(view: View) {
        if (listAudio.isEmpty()) {
            audioHelper =
                AudioHelper(view, context)
        } else {
            if (recordingLabel.visibility == View.VISIBLE)
                recordingLabel.visibility = View.GONE
            adapterAudio = AudioListAdapter(
                requireContext(),
                listAudio,
                this,
                0
            )
            audioRecycleView.adapter = adapterAudio
        }

        var uriAudio = "null"

        audioBtn.setOnClickListener {
            if (recordingLabel.visibility == View.VISIBLE)
                recordingLabel.visibility = View.GONE
            when (recordingState) {
                0 -> {
                    //inizio a registrare
                    uriAudio = audioHelper.startRecording(audioBtn, chronometer)
                    //cambio stato
                    recordingState = 1
                }
                1 -> {
                    listAudio.add(uriAudio)
                    adapterAudio =
                        AudioListAdapter(
                            requireContext(),
                            listAudio,
                            this,
                            0
                        )
                    audioRecycleView.adapter = adapterAudio
                    //fermo la registrazione
                    audioHelper.stopRecording(audioBtn, chronometer)
                    //cambio stato
                    recordingState = 0
                }
            }
        }
    }

    private fun getData(): Dream {
        val id: Int? = restoreDream?.dreamID
        return Dream(
            id ?: 0,
            date = datePickerBtn.text.toString(),
            title = title.text.toString(),
            description = description.text.toString(),
            tags = tags,
            rate = ratingDream.rating,
            audios = listAudio,
            images = imageList
        )
    }

    private fun initFloatAction(view: View) {
        //inizializzo con valori di default il mio sogno
        newDream = Dream(
            0,
            "00/00/00",
            "empty",
            "empty",
            mutableListOf(),
            2.5F,
            mutableListOf(),
            mutableListOf()
        )

        floatingActionButton.setOnClickListener {
            if (editable) {
                (activity as MainActivity?)?.closeKeyboard()
                //aggiungere un controllo dell'inserimento di titolo e descrizione
                val titleEmpty = title.text.isBlank()
                val textDescriptionEmpty = description.text.isBlank()
                val audioDescriptionEmpty = listAudio.isEmpty()

                //controllo presenza dati nei vari form e display di un toast nel caso non ci siano
                if (titleEmpty && textDescriptionEmpty && audioDescriptionEmpty) {
                    Snackbar.make(
                        view,
                        resources.getString(R.string.insert_t_d),
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else if (titleEmpty) {
                    Snackbar.make(
                        view,
                        resources.getString(R.string.insert_t),
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                } else if (textDescriptionEmpty && audioDescriptionEmpty) {
                    Snackbar.make(
                        view,
                        resources.getString(R.string.insert_d),
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                } else {
                    //recupero i dati del nuovo dream
                    newDream = getData()
                    //inserisco il nuovo sogno nel db ad aggiorno il tutto
                    viewModel.insertNewDream(newDream)
                    //salvataggio sogno cloud
                    DbUtil(auth, Firebase.firestore).saveDream(newDream)
                    //momentaneamente rimando alla home
                    findNavController().navigate(R.id.home_dest)
                    Snackbar.make(
                        view,
                        "${resources.getString(R.string.insert_t_d)} ${newDream.title}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            } else {
                //change icon
                floatingActionButton.setImageResource(R.drawable.ic_save)

                //unlock fields
                changeStateFields(true)

                editable = true

                setDialogBack(false)

            }
        }
    }

    private fun changeStateFields(state: Boolean) {
        //datepicker
        datePickerBtn.isEnabled = state

        //titolo
        title.isFocusableInTouchMode = state

        //descrizione
        description.isFocusableInTouchMode = state

        //tags
        insertTag.isFocusableInTouchMode = state
        if (state.not()) {
            insertTagBtn.visibility = View.INVISIBLE
            if (tags.isEmpty())
                insertTag.setText(getString(R.string.no_tags_label))
        } else {
            insertTag.setText("")
            insertTagBtn.visibility = View.VISIBLE
        }
        var mode = 0
        if (state.not())
            mode = 1
        adapterTag =
            TagListAdapter(
                requireContext(),
                tags,
                this,
                mode
            )
        tagsRecycleView.adapter = adapterTag

        //rating
        ratingDream.setIsIndicator(state.not())

        //audio
        if (restoreDream?.audios?.size == 0) {
            recording_label.visibility = View.VISIBLE
            if (state.not())
                recording_label.text = getString(R.string.no_recording_label)
            else
                recording_label.text = getString(R.string.how_to_record_label)
        }else{
            recording_label.visibility = View.INVISIBLE
            adapterAudio =
                AudioListAdapter(
                    requireContext(),
                    listAudio,
                    this,
                    mode
                )
            audioRecycleView.adapter = adapterAudio
            audioBtn.isEnabled = state
        }


        //drawing
        addDraw.isEnabled = state

        //image
        addImage.isEnabled = state
    }

    private fun initTags(view: View) {
        adapterTag = TagListAdapter(
            requireContext(),
            tagSet.toList(),
            this,
            0
        )
        //assegno il mio adapter alla mia RecyclerView
        tagsRecycleView.adapter = adapterTag

        val emptyInputSnackbar =
            Snackbar.make(view, resources.getString(R.string.empty_tag), Snackbar.LENGTH_SHORT)
        val dublicateSnackbar =
            Snackbar.make(view, resources.getString(R.string.duplicate_tag), Snackbar.LENGTH_SHORT)

        insertTagBtn.setOnClickListener {
            //prendo in input il tag e lo aggiungo alla lista
            insertTag(dublicateSnackbar, emptyInputSnackbar)
        }

        insertTag.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                insertTag(dublicateSnackbar, emptyInputSnackbar)
                return@OnKeyListener true
            }
            false
        })
    }

    private fun insertTag(
        dublicateSnackbar: Snackbar,
        emptyInputSnackbar: Snackbar
    ) {
        val input = insertTag.text.toString().toLowerCase(Locale.ROOT)
        if (input != "") {
            if (tagSet.add(input).not())
                dublicateSnackbar.show()
        } else {
            emptyInputSnackbar.show()
        }
        //sort list
        tags = tagSet.toMutableList()
        //creo il mio adapter
        adapterTag = TagListAdapter(
            requireContext(),
            tags,
            this,
            0
        )
        //assegno il mio adapter alla mia RecyclerView
        tagsRecycleView.adapter = adapterTag
        //resetto la textfield per inserire i tag
        insertTag.setText("")
    }

    private fun initBackButton() {
        cancelBtn.setOnClickListener {
            (activity as MainActivity?)?.closeKeyboard()
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
            val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val dateSelected = dateFormatter.parse(dateClicked!!)
            val yearFormatter = SimpleDateFormat("yyyy", Locale.US)
            val monthFormatter = SimpleDateFormat("MM", Locale.US)
            val dayFormatter = SimpleDateFormat("dd", Locale.US)

            year = yearFormatter.format(dateSelected?.time).toInt()
            month = monthFormatter.format(dateSelected?.time).toInt()
            day = dayFormatter.format(dateSelected?.time).toInt()

        } else {
            //imposto la data di oggi come data di default
            val calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH) + 1
            year = calendar.get(Calendar.YEAR)
        }

        val finalDate: String = if (datePicked == null) {
            "$day/$month/$year"
        } else {
            datePicked as String
        }

        datePickerBtn.text = finalDate

        datePickerBtn.setOnClickListener {
            (activity as MainActivity?)?.closeKeyboard()
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, month, dayOfMonth ->
                    datePicked = "$dayOfMonth/${month + 1}/$year"
                    datePickerBtn.text = datePicked
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

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1000) {
            val uriSourceFile = data?.data

            val cr = context?.contentResolver
            val mime = MimeTypeMap.getSingleton()
            val type = mime.getExtensionFromMimeType(cr?.getType(uriSourceFile!!))
            val sourceFile = File(getRealPathFromURI(uriSourceFile)!!)

            val fileName = "${Dream.createUniqueName()}.$type"
            val destFile = File(Dream.getBasePath(requireContext(), FOLDER_IMAGE), fileName)
            sourceFile.copyTo(destFile, true)
            imageList.add(fileName)
            adapterImage = ImageListAdapter(
                requireContext(),
                imageList
            )
            images_recyclerview.adapter = adapterImage
        }
    }

    private fun getRealPathFromURI(contentUri: Uri?): String? {
        @Suppress("DEPRECATION")
        val proj = arrayOf(MediaStore.Audio.Media.DATA)
        val cursor: Cursor? = context?.contentResolver?.query(contentUri!!, proj, null, null, null)

        @Suppress("DEPRECATION")
        val columnIndex: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        cursor?.moveToFirst()
        val toRtn = cursor?.getString(columnIndex!!)
        cursor?.close()
        return toRtn
    }

    override fun onTagItemListener(tag: String, position: Int) {
        //elimino tag
        tagSet.remove(tag)
        tags = tagSet.toMutableList()
        //creo il mio adapter
        adapterTag = TagListAdapter(
            requireContext(),
            tags,
            this,
            0
        )
        //assegno il mio adapter alla mia RecyclerView
        tagsRecycleView.adapter = adapterTag
    }

    override fun onAudioItemListener(
        titleRecording: String,
        playerIcon: ImageView,
        holder: AudioListAdapter.ViewHolder
    ) {
        audioHelper =
            AudioHelper(requireView(), context)
        audioHelper.play(titleRecording, playerIcon)

        //gestione stop riproduzione audio (non la migliore delle implementazionni, da rivedere in futuro
        holder.itemView.setOnClickListener {
            audioHelper.pause()
            holder.itemView.setOnClickListener(null)
            adapterAudio = AudioListAdapter(
                requireContext(),
                listAudio,
                this,
                0
            )
            audioRecycleView.adapter = adapterAudio
        }
    }

    override fun onAudioItemLongListener(
        titleRecording: String,
        playerIcon: ImageView,
        playerLabel: TextView,
        holder: AudioListAdapter.ViewHolder
    ) {
        playerIcon.setImageResource(R.drawable.ic_trash)
        playerLabel.text = getString(R.string.delete_label)
        holder.itemView.setOnClickListener {
            audioHelper.delete(titleRecording)
            listAudio.remove(titleRecording)
            adapterAudio = AudioListAdapter(
                requireContext(),
                listAudio,
                this,
                0
            )
            audioRecycleView.adapter = adapterAudio
        }
    }

    override fun onImageItemListener(titleRecording: String, playerIcon: ImageView) {

    }
}




