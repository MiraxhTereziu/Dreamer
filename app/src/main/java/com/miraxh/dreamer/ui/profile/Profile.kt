package com.miraxh.dreamer.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.miraxh.dreamer.R
import java.net.URL

class Profile : Fragment() {

    private lateinit var homeTitle: TextView
    private lateinit var imageProfile: ImageView
    private lateinit var nameInfo: TextView
    private lateinit var surnameInfo: TextView
    private lateinit var emailInfo: TextView
    private lateinit var passwordInfo: TextView


    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        homeTitle = view.findViewById(R.id.toolbar_title)
        imageProfile = view.findViewById(R.id.profile_image)

        nameInfo = view.findViewById(R.id.name_info)
        surnameInfo = view.findViewById(R.id.surname_info)
        emailInfo = view.findViewById(R.id.email_info)
        passwordInfo = view.findViewById(R.id.password_info)

        //setting user name
        homeTitle.text = "My profile"

        //setting user image profile
        Glide.with(requireContext())
            .load(user?.photoUrl)
            .into(imageProfile)

        //display user info
        val name = user?.displayName?.split(" ")

        nameInfo.text = "Name: ${name?.get(0).toString()}"
        surnameInfo.text = "Surname: ${name?.get(1).toString()}"
        emailInfo.text = "Email: ${user?.email}"
        passwordInfo.text = "Password: ************"

        return view
    }

}