package com.miraxh.dreamer.ui.splash

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.miraxh.dreamer.R
import com.miraxh.dreamer.util.PERMISSION_CODE

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //controllo se ho i permessi di scritture e registrazione audio
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            displayMainFragment(true)
        } else {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.RECORD_AUDIO
                ),
                PERMISSION_CODE
            )
        }

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_CODE) {
            //controllo
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                displayMainFragment(true)
            }else{
                displayMainFragment(false)
            }
        }
    }

    fun displayMainFragment(creationPermission: Boolean) {
        //faccio in modo di navigare alla home però in caso di click del pulsante indietro
        // l'applicazione si chiuderà e non tornerà a questo fragment

        val bundle = Bundle()
        bundle.putBoolean("creationPermission",creationPermission)

        findNavController().navigate(
            R.id.home_dest,
            bundle,
            NavOptions.Builder()
                .setPopUpTo(R.id.splash_dest, true)
                .build()
        )
    }

}
