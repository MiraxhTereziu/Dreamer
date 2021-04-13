package com.miraxh.dreamer.util

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.*

class DbUtil(private val auth: FirebaseAuth, private val db: FirebaseFirestore) {
    private var user: FirebaseUser? = auth.currentUser

    fun saveUser() {
        val userInfo = hashMapOf(
            "email" to "${user?.email}",
            "name" to "${user?.displayName}".toLowerCase(Locale.ROOT),
            "profilePic" to "${user?.photoUrl}"
        )
        db.collection("user").document(user?.uid.toString())
            .set(userInfo)
            .addOnSuccessListener {
                Log.d(
                    "firestoreDebug",
                    "DocumentSnapshot successfully written!"
                )
            }
            .addOnFailureListener { e -> Log.w("firestoreDebug", "Error writing document", e) }
    }

    fun getUserByName(searchName: String): Task<QuerySnapshot> {
        return db.collection("user")
            .whereGreaterThanOrEqualTo("name", searchName.toLowerCase(Locale.ROOT))
            .whereLessThan("name", searchName.toLowerCase(Locale.ROOT) + "z")
            .get()
    }

    fun getUserByID(id: String): Task<DocumentSnapshot> {
        return db.collection("user")
            .document(id)
            .get()

    }

    fun saveFollowing(idHost: String, idReciver: String) {
        val followCreate = hashMapOf(
            "exist" to "true"
        )

        db.collection("following")
            .document(idHost)
            .collection("userFollowing")
            .document(idReciver)
            .set(followCreate)
    }

    fun deleteFollowing(idHost: String, idReciver: String) {
        db.collection("following")
            .document(idHost)
            .collection("userFollowing")
            .document(idReciver)
            .delete()
    }


    fun getFollowing(): CollectionReference {
        return db.collection("following").document("${user?.uid}").collection("userFollowing")
    }
}