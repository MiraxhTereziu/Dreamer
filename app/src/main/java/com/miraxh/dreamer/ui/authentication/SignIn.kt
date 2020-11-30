package com.miraxh.dreamer.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.miraxh.dreamer.MainActivity
import com.miraxh.dreamer.R


class SignIn : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var gso: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleBtn: FrameLayout
    private lateinit var facebookBtn: FrameLayout
    private lateinit var emailUpBtn: FrameLayout
    private lateinit var emailInBtn: FrameLayout

    private lateinit var callbackManager: CallbackManager

    companion object {
        private const val RC_SIGN_IN = 120
        private const val TAG = "DebugSignIn"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as MainActivity?)?.disableDrawer()

        //firebase init
        auth = Firebase.auth

        //google auth
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signin, container, false)
        googleBtn = view.findViewById(R.id.layout_google_signin)
        facebookBtn = view.findViewById(R.id.layout_facebook_signin)
        emailUpBtn = view.findViewById(R.id.layout_mail_signup)
        emailInBtn = view.findViewById(R.id.layout_mail_signin)

        //google sign in
        googleBtn.setOnClickListener {
            signInGoogle()
        }

        //facebook sign in
        signInFacebook(view)

        //email sign up
        emailUpBtn.setOnClickListener {
            findNavController().navigate(
                R.id.signUpEmail_dest,
                null
            )
        }

        //email sign in
        emailInBtn.setOnClickListener {
            findNavController().navigate(
                R.id.signInEmail_dest,
                null
            )
        }

        return view
    }

    private fun signInFacebook(view: View) {
        callbackManager = CallbackManager.Factory.create()
        val loginButton = view.findViewById<LoginButton>(R.id.login_button)

        facebookBtn.setOnClickListener {
            loginButton.callOnClick()
        }
        loginButton.fragment = this
        loginButton.setPermissions("email", "public_profile")
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.i(TAG, loginResult.accessToken.toString())
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException) {
                Toast.makeText(
                    requireActivity(),
                    error.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }


    // [START auth_with_facebook]
    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    findNavController().navigate(
                        R.id.home_dest,
                        null
                    )
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        requireActivity(),
                        task.exception.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    LoginManager.getInstance().logOut()
                }
            }
    }


    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Toast.makeText(
                        requireActivity(),
                        task.exception.toString(),
                        Toast.LENGTH_LONG
                    ).show()
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

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Log.i("MyNameIs", user?.displayName ?: "No name")
                    findNavController().navigate(
                        R.id.home_dest,
                        null
                    )
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


