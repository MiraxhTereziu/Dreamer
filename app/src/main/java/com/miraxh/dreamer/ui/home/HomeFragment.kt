package com.miraxh.dreamer.ui.home

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
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
import com.google.android.material.snackbar.Snackbar
import com.miraxh.dreamer.MainActivity
import com.miraxh.dreamer.R
import com.miraxh.dreamer.data.Day
import com.miraxh.dreamer.data.dream.Dream
import com.miraxh.dreamer.ui.toolbar.ToolbarListAdapter
import com.miraxh.dreamer.util.DATE_CLICKED
import com.miraxh.dreamer.util.PERMISSION
import com.miraxh.dreamer.util.PERMISSION_CODE
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment(), ToolbarListAdapter.DayListener {

    private lateinit var daysRecycleView: RecyclerView
    private lateinit var dreamRecyclerView: RecyclerView
    private lateinit var adapterDay: ToolbarListAdapter
    private lateinit var adapterDream: DreamListAdapter
    private lateinit var viewModel: HomeViewModel
    private lateinit var addActionButton: FloatingActionButton

    private lateinit var daysState: Parcelable
    private lateinit var dreamState: Parcelable
    private var creationPermission = false

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            creationPermission = it.getBoolean(PERMISSION)
        }
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
            if (creationPermission){
                findNavController().navigate(R.id.add_dest)
            }else{
                checkPermission()
            }
        }
    }

    private fun checkPermission(){
        Log.i("permission_test","request")
        requestPermissions(
            arrayOf(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.RECORD_AUDIO
            ),
            PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_CODE) {
            //controllo
            creationPermission = grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
            if (creationPermission==false){
                view?.let {
                    Snackbar.make(it, "You can't use this function without permission", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
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
            adapterDay = ToolbarListAdapter(requireContext(), it, this)
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
        //cambio lo stato di un particolare giorno dell'agenda in caso di click (vecchia implementazione)
        val args = Bundle()
        args.putString(DATE_CLICKED,day.date)

        if (creationPermission){
            findNavController().navigate(R.id.add_dest,args)
        }else{
            checkPermission()
        }
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

