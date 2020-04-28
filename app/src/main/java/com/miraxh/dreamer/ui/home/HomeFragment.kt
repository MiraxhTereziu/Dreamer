package com.miraxh.dreamer.ui.home

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.miraxh.dreamer.MainActivity
import com.miraxh.dreamer.R
import com.miraxh.dreamer.data.Day
import com.miraxh.dreamer.data.dream.Dream
import com.miraxh.dreamer.ui.toolbar.ToolbarRecycleAdapter
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment(), ToolbarRecycleAdapter.DayListener {

    private lateinit var daysRecycleView: RecyclerView
    private lateinit var dreamRecyclerView: RecyclerView
    private lateinit var adapterDay: ToolbarRecycleAdapter
    private lateinit var adapterDream: DreamListAdapter
    private lateinit var daysState: Parcelable
    private lateinit var viewModel: HomeViewModel
    private lateinit var addActionButton: FloatingActionButton

    companion object {
        fun newInstance() = HomeFragment()
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
        //drawerLayout = view.findViewById(R.id.drawer_layout)
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
    private fun dreamListLoader(){
        //mi metto in ascolto per eventuali cambiamenti alla lista dei sogni
        viewModel.dreamData.observe(viewLifecycleOwner, Observer {
            //ordino la lista in base al giorno di creazione
            (it as MutableList<Dream>).sortByDescending { it.dreamID}
            //in caso un update della lista dei sogni entro in questo metodo dove mi viene passata la nuova lista
            //creo un nuovo adapter da essegnare al mio RecyclerView
            adapterDream = DreamListAdapter(requireContext(),it)
            //assegno il mio adapter alla mia RecyclerView
            dreamRecyclerView.adapter = adapterDream
            //update toolbar
            viewModel.updateToolbar()
        })
    }

    //metodo per inizializzare la toolbar e "l'agenda dei sogni" al suo interno
    private fun initToolbar(){
        drawer_icon.setOnClickListener{
            (activity as MainActivity?)?.openDrawer()
        }
        //inizializzo lo stato di scorrimento dell'agenda per utilizzi futuri
        daysState = saveStateRV(daysRecycleView)
        //mi metto in ascolto per eventuali cambiamenti alla lista dei sogni
        viewModel.daysData.observe(viewLifecycleOwner, Observer {
            //in caso un update della lista dei sogni entro in questo metodo dove mi viene passata la nuova lista
            adapterDay = ToolbarRecycleAdapter(requireContext(), it, this)
            //assegno il mio adapter alla mia RecyclerView
            daysRecycleView.adapter = adapterDay
            //ripristino lo stato di scorrimento della mia agenda nel toolbar
            restoreStateRV(daysState, daysRecycleView)
        })
    }

    //metodo per gestire il click di un particolare giorno dell'agenda
    override fun onDayItemListener(day: Day, position: Int) {
        //salvo lo stato di scorrimento
        daysState = saveStateRV(daysRecycleView)
        //cambio lo stato di un particolare giorno dell'agenda in caso di click
        viewModel.changeState(day)
    }

    //metodo per salvare lo stato dello scorrimento della lista
    private fun saveStateRV(recyclerView: RecyclerView): Parcelable {
        return recyclerView.layoutManager?.onSaveInstanceState()!!
    }

    //metodo per ripristinare lo stato di scorrimento della lista
    private fun restoreStateRV(state: Parcelable, recyclerView: RecyclerView) {
        recyclerView.layoutManager!!.onRestoreInstanceState(state)
    }
}

