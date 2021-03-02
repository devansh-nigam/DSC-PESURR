package com.example.dscpesurr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_users_display.*

class UsersDisplayActivity : AppCompatActivity()
{
    private val db = Firebase.firestore
    val settings = firestoreSettings {
        isPersistenceEnabled = true
    }

    var userAdapter:UserAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_display)

        db.firestoreSettings = settings

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val query=db.collection("Users")
        val firestoreRecyclerOptions:FirestoreRecyclerOptions<UsersModel> = FirestoreRecyclerOptions.Builder<UsersModel>().
                setQuery(query,UsersModel::class.java).build()

        userAdapter= UserAdapter(firestoreRecyclerOptions)

        rv.layoutManager=LinearLayoutManager(this)
        rv.adapter=userAdapter
    }

    override fun onStart() {
        super.onStart()
        userAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        userAdapter!!.stopListening()
    }
}