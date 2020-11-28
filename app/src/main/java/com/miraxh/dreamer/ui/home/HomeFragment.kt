package com.miraxh.dreamer.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.miraxh.dreamer.MainActivity
import com.miraxh.dreamer.R
import com.miraxh.dreamer.data.Day
import com.miraxh.dreamer.data.dream.Dream
import com.miraxh.dreamer.ui.toolbar.ToolbarListAdapter
import com.miraxh.dreamer.util.DATE_CLICKED
import com.miraxh.dreamer.util.EDITABLE
import com.miraxh.dreamer.util.FOLDER_IMAGE
import com.miraxh.dreamer.util.RESTORE_DREAM
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment(), ToolbarListAdapter.DayListener, DreamListAdapter.DreamListener {

    private lateinit var daysRecycleView: RecyclerView
    private lateinit var dreamRecyclerView: RecyclerView
    private lateinit var adapterDay: ToolbarListAdapter
    private lateinit var adapterDream: DreamListAdapter
    private lateinit var viewModel: HomeViewModel
    private lateinit var addActionButton: FloatingActionButton
    private lateinit var homeTitle: TextView
    private lateinit var imageProfile: ImageView


    private lateinit var daysState: Parcelable

    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        //inizializzaizone RecyclerView
        dreamRecyclerView = view.findViewById(R.id.dream_recyclerview)
        daysRecycleView = view.findViewById(R.id.days_recyclerview)
        addActionButton = view.findViewById(R.id.add_action_button)
        homeTitle = view.findViewById(R.id.toolbar_title)
        imageProfile = view.findViewById(R.id.profile_image)
        (activity as MainActivity?)?.enableDrawer()

        user = auth.currentUser
        val name = user?.displayName?.split(" ")?.get(0)
        homeTitle.text = name.toString()

        Glide.with(requireContext())
            .load(user?.photoUrl)
            .into(imageProfile)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //inizializzazione viewModel
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        //metodo per inizializzare lista principale dei sogni
        dreamListLoader()
        //metodo per inizializzare la toolbar e "l'agenda dei sogni" al suo interno
        initToolbar()
        addActionButton.setOnClickListener {
            findNavController().navigate(R.id.add_dest)
        }
    }

    //metodo per inizializzare lista principale dei sogni
    private fun dreamListLoader() {
        Log.i("finaltest", "!empty")
        //mi metto in ascolto per eventuali cambiamenti alla lista dei sogni
        viewModel.dreamData.observe(viewLifecycleOwner, Observer {
            //ordino la lista in base al giorno di creazione
            (it as MutableList<Dream>).sortByDescending { dream -> dream.dreamID }
            //in caso un update della lista dei sogni entro in questo metodo dove mi viene passata la nuova lista
            //creo un nuovo adapter da essegnare al mio RecyclerView
            Log.i("finaltest", it.size.toString())
            adapterDream = DreamListAdapter(requireContext(), it, this)
            //assegno il mio adapter alla mia RecyclerView
            dreamRecyclerView.adapter = adapterDream
            //update toolbar
            viewModel.updateToolbar()
        })

    }

    //metodo per inizializzare la toolbar e "l'agenda dei sogni" al suo interno
    private fun initToolbar() {
        drawer_icon.setOnClickListener {
            (activity as MainActivity?)?.openDrawer()
        }
        //inizializzo lo stato di scorrimento dell'agenda per utilizzi futuri
        daysState = saveStateRV(daysRecycleView)
        //mi metto in ascolto per eventuali cambiamenti alla lista dei sogni
        viewModel.daysData.observe(viewLifecycleOwner, Observer {
            //in caso un update della lista dei sogni entro in questo metodo dove mi viene passata la nuova lista
            adapterDay = ToolbarListAdapter(requireContext(), it, this)
            //assegno il mio adapter alla mia RecyclerView
            daysRecycleView.adapter = adapterDay
            //ripristino lo stato di scorrimento della mia agenda nel toolbar
            restoreStateRV(daysState, daysRecycleView)
        })
    }


    //metodo per salvare lo stato dello scorrimento della lista
    private fun saveStateRV(recyclerView: RecyclerView): Parcelable {
        return recyclerView.layoutManager?.onSaveInstanceState()!!
    }

    //metodo per ripristinare lo stato di scorrimento della lista
    private fun restoreStateRV(state: Parcelable, recyclerView: RecyclerView) {
        recyclerView.layoutManager!!.onRestoreInstanceState(state)
    }

    //metodo per gestire il click di un particolare giorno dell'agenda
    override fun onDayItemListener(day: Day, position: Int) {
        //salvo lo stato di scorrimento
        daysState = saveStateRV(daysRecycleView)
        //cambio lo stato di un particolare giorno dell'agenda in caso di click (vecchia implementazione)
        val args = Bundle()
        args.putString(DATE_CLICKED, day.date)
        findNavController().navigate(R.id.add_dest, args)
    }

    override fun onDreamItemListener(dream: Dream, position: Int) {
        val dreamBundle = Bundle()
        dreamBundle.putSerializable(RESTORE_DREAM, dream)
        dreamBundle.putBoolean(EDITABLE, false)
        findNavController().navigate(R.id.add_dest, dreamBundle)
    }
}

