package com.miraxh.dreamer.ui.authentication

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.miraxh.dreamer.R
import com.miraxh.dreamer.util.FROM_SIGNIN

class SingInEmail : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var signInBtn: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_singin_email, container, false)
        email = view.findViewById(R.id.email_input)
        password = view.findViewById(R.id.password_input)
        signInBtn = view.findViewById(R.id.layout_signin)

        signInBtn.setOnClickListener {
            if(checkForm())
                signInEmail()
        }
        return view
    }

    private fun checkForm(): Boolean {
        var tortn = false
        if ((TextUtils.isEmpty(email.text)).not() &&
            (TextUtils.isEmpty(password.text)).not()
        ) {
            tortn = true
        } else {
            Snackbar.make(
                requireView(),
                "Empty field not allowed!",
                Snackbar.LENGTH_SHORT
            ).show()
            /*Toast.makeText(
                requireActivity(),
                "Empty field not allowed!",
                Toast.LENGTH_LONG
            ).show()*/
        }
        return tortn
    }

    private fun signInEmail() {
        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    findNavController().navigate(
                        R.id.home_dest,
                        null
                    )
                } else {
                    /*Toast.makeText(
                        requireActivity(),
                        task.exception.toString(),
                        Toast.LENGTH_LONG
                    ).show()*/
                    Snackbar.make(
                        requireView(),
                        "Password or email are not correct!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
    }

}