package com.miraxh.dreamer.util

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class DbUtil(private val auth: FirebaseAuth, private val db: FirebaseFirestore) {
    private var user:FirebaseUser? = auth.currentUser

    fun saveUser(){
        val userInfo = hashMapOf(
            "email" to "${user?.email}",
            "name" to "${user?.displayName}",
            "profilePic" to "${user?.photoUrl}"
        )
        db.collection("user").document(user?.uid.toString())
            .set(userInfo)
            .addOnSuccessListener { Log.d("firestoreDebug", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("firestoreDebug", "Error writing document", e) }
    }

    fun getUser():CollectionReference{
        return db.collection("user")
    }
}