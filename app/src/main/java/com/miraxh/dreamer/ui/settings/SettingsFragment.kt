package com.miraxh.dreamer.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.miraxh.dreamer.MainActivity
import com.miraxh.dreamer.R

class SettingsFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private lateinit var viewModel: SettingsViewModel
    private lateinit var toolbar: ConstraintLayout
    private lateinit var toolbarTitle : TextView
    private lateinit var drawerButton: ImageView
    private lateinit var logoutBtn : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.settings_fragment, container, false)
        toolbar = view.findViewById(R.id.toolbar_settings)
        logoutBtn = view.findViewById(R.id.logout_title)

        toolbarTitle = toolbar.findViewById(R.id.toolbar_title_normal)
        drawerButton = toolbar.findViewById(R.id.drawer_icon_normal)

        toolbarTitle.text =  resources.getString(R.string.settings_title)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        drawerButton.setOnClickListener {
            (activity as MainActivity?)?.openDrawer()
        }

        logoutBtn.setOnClickListener {
            auth.signOut()
            LoginManager.getInstance().logOut()
            findNavController().navigate(
                R.id.signin_dest,
                null
            )
        }
    }

}
