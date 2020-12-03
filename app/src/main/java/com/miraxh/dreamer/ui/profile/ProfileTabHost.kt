package com.miraxh.dreamer.ui.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TabHost
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.login.LoginManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.miraxh.dreamer.MainActivity
import com.miraxh.dreamer.R
import com.miraxh.dreamer.data.user.User
import com.miraxh.dreamer.util.DbUtil

class ProfileTabHost : Fragment(), UsersListAdapter.UserListener {

    private lateinit var profileView: ConstraintLayout
    private lateinit var searchView: ConstraintLayout

    private lateinit var imageProfile: ImageView
    private lateinit var homeTitle: TextView
    private lateinit var backBtn: ImageView
    private lateinit var nameInfo: TextView
    private lateinit var surnameInfo: TextView
    private lateinit var emailInfo: TextView
    private lateinit var passwordInfo: TextView
    private lateinit var userSearch: TextInputEditText
    private lateinit var logoutBtn: FrameLayout

    private lateinit var usersSearchRecyclerView: RecyclerView
    private lateinit var adapterUsersSearch: UsersListAdapter

    private lateinit var usersFollowingRecyclerView: RecyclerView
    private lateinit var adapterUsersFollowing: UsersListAdapter

    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser
    }

    private lateinit var tabHost: TabHost
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_tab_host, container, false)

        //setting up tabs system
        tabHost = view.findViewById(R.id.tabhost)
        tabHost.setup()
        var spec = tabHost.newTabSpec("profile")
        spec.setContent(R.id.tab1)
        spec.setIndicator("profile")
        tabHost.addTab(spec)

        spec = tabHost.newTabSpec("search")
        spec.setContent(R.id.tab2)
        spec.setIndicator("search")
        tabHost.addTab(spec)

        //getting ui elements
        profileView = view.findViewById(R.id.layout_profile)
        searchView = view.findViewById(R.id.layout_search)

        homeTitle = view.findViewById(R.id.toolbar_title)
        backBtn = view.findViewById(R.id.back)

        nameInfo = profileView.findViewById(R.id.name_info)
        surnameInfo = profileView.findViewById(R.id.surname_info)
        emailInfo = profileView.findViewById(R.id.email_info)
        passwordInfo = profileView.findViewById(R.id.password_info)
        imageProfile = profileView.findViewById(R.id.profile_image)
        logoutBtn = profileView.findViewById(R.id.layout_logout)

        userSearch = searchView.findViewById(R.id.search_user)
        usersSearchRecyclerView = searchView.findViewById(R.id.users_search_recyclerview)

        usersFollowingRecyclerView = searchView.findViewById(R.id.users_following_recyclerview)

        //setting user name
        homeTitle.text = "My profile"

        //setting user image profile
        if (user?.photoUrl != null) {

            var photoUrl = user?.photoUrl.toString()
            photoUrl = "$photoUrl?height=500"
            Glide.with(requireContext())
                .load(photoUrl)
                .into(imageProfile)
        }
        //display user info
        val name = user?.displayName?.split(" ")

        nameInfo.text = "Name: ${name?.get(0).toString()}"
        surnameInfo.text = "Surname: ${name?.get(1).toString()}"
        emailInfo.text = "Email: ${user?.email}"
        passwordInfo.text = "Password: ************"

        //logout action
        logoutBtn.setOnClickListener {
            auth.signOut()
            LoginManager.getInstance().logOut()
            findNavController().navigate(
                R.id.signin_dest,
                null
            )
        }

        //back btn listener
        backBtn.setOnClickListener {
            (activity as MainActivity?)?.closeKeyboard()
            findNavController().navigateUp()
        }

        //getting user following
        val following = DbUtil(auth, Firebase.firestore).getFollowing()

        val userFollowList = mutableSetOf<User>()
        following.get()
            .addOnSuccessListener { document ->
                document.forEach { userFollowing ->
                    Log.d("followingDebug", userFollowing.id)
                    DbUtil(auth, Firebase.firestore).getUserByID(userFollowing.id)
                        .addOnSuccessListener {
                            userFollowList.add(
                                User(
                                    it.id,
                                    it.data?.get("email") as String,
                                    it.data?.get("name") as String,
                                    it.data?.get("profilePic") as String
                                )
                            )
                            updateFollowingList(userFollowList.toMutableList())
                        }
                }
            }


        val doubleCheck = userFollowList
        userSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank().not()) {
                    val userSearchList = mutableListOf<User>()
                    val users = DbUtil(auth, Firebase.firestore).getUserByName(s.toString())
                    users
                        .addOnSuccessListener { document ->
                            document.forEach {
                                val newUser = User(
                                    it.id,
                                    it.data["email"] as String,
                                    it.data["name"] as String,
                                    it.data["profilePic"] as String
                                )
                                if(doubleCheck.add(newUser))
                                    userSearchList.add(newUser)
                            }
                            updateSearchList(userSearchList)
                        }
                } else {
                    updateSearchList(mutableListOf())
                }
            }
        })

        return view
    }

    fun updateSearchList(userList: MutableList<User>) {
        adapterUsersSearch = UsersListAdapter(requireContext(), userList.toList(), this, true)
        //assegno il mio adapter alla mia RecyclerView
        usersSearchRecyclerView.adapter = adapterUsersSearch
    }

    fun updateFollowingList(userList: MutableList<User>) {
        adapterUsersFollowing = UsersListAdapter(requireContext(), userList.toList(), this, false)
        //assegno il mio adapter alla mia RecyclerView
        usersFollowingRecyclerView.adapter = adapterUsersFollowing
    }

    override fun onUserItemListener(selectedUser: User, position: Int, follow: Boolean) {
        if(follow){
            Log.d("followingDebug", selectedUser.email)
            DbUtil(auth, Firebase.firestore).saveFollowing(user?.uid.toString(),selectedUser.ID)
        }
    }
}