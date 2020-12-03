package com.miraxh.dreamer.ui.authentication

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.miraxh.dreamer.R
import com.miraxh.dreamer.util.DbUtil

class SignUpEmail : Fragment() {

    private lateinit var auth: FirebaseAuth

    private lateinit var email: TextInputEditText
    private lateinit var name: TextInputEditText
    private lateinit var surname: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var repeatPassword: TextInputEditText
    private lateinit var registerBtn: FrameLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup_email, container, false)
        name = view.findViewById(R.id.name_input)
        surname = view.findViewById(R.id.surname_input)
        repeatPassword = view.findViewById(R.id.password_repeat_input)
        email = view.findViewById(R.id.email_input)
        password = view.findViewById(R.id.password_input)
        registerBtn = view.findViewById(R.id.layout_register)

        registerBtn.setOnClickListener {
            if (checkForm())
                register()
        }
        return view
    }

    private fun checkForm(): Boolean {
        var tortn = false
        if ((TextUtils.isEmpty(email.text)).not() &&
            (TextUtils.isEmpty(name.text)).not() &&
            (TextUtils.isEmpty(surname.text)).not() &&
            (TextUtils.isEmpty(password.text)).not() &&
            (TextUtils.isEmpty(repeatPassword.text)).not()
        ) {
            if (password.text.toString() == repeatPassword.text.toString()) {
                tortn = true
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Password and repeat password must be the same",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(
                requireActivity(),
                "Empty field not allowed!",
                Toast.LENGTH_LONG
            ).show()
        }
        return tortn
    }


    private fun register() {
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    val profileUpdates =
                        UserProfileChangeRequest
                            .Builder()
                            .setDisplayName("${name.text} ${surname.text}")
                            .build()

                    user?.updateProfile(profileUpdates)?.addOnCompleteListener {
                        //saving new user info in the db
                        DbUtil(auth,Firebase.firestore).saveUser()

                        //navigate to homepage
                        findNavController().navigate(
                            R.id.home_dest,
                            null
                        )
                    }
                } else {
                    Toast.makeText(
                        requireActivity(),
                        task.exception.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }


}