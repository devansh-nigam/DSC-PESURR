package com.example.dscpesurr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_dashboard_onstart.*
import kotlinx.android.synthetic.main.activity_users_display.*

class DashboardActivity : AppCompatActivity()
{
    private lateinit var mAuth: FirebaseAuth
    private var layoutManager: RecyclerView.LayoutManager?=null
    //private var adapterUpcomingEvents:RecyclerView.Adapter<RecyclerViewAdapter_UpcomingEvents.ViewHolder>?=null
    //private var adapterPastEvents:RecyclerView.Adapter<RecyclerViewAdapter_PastEvents.ViewHolder>?=null
    private var adapterOrganizers:RecyclerView.Adapter<RecyclerViewAdapter_Organizers.ViewHolder>?=null
    //private lateinit var iT:ArrayList<String>
    //private lateinit var iD:ArrayList<String>

    private val db = Firebase.firestore
    val settings = firestoreSettings {
        isPersistenceEnabled = true
    }

    var upcomingEventAdapter:EventAdapter?=null
    var pastEventAdapter:EventAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_onstart)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        mAuth= FirebaseAuth.getInstance()

        val actionBar = supportActionBar
        actionBar!!.title = "Dashboard"

//        layoutManager=LinearLayoutManager(this)
//        (layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
//        recyclerViewUpComingEvents.layoutManager=layoutManager
//
//        val e=UpcomingEvents().loadEvents()
//
//        adapterUpcomingEvents = RecyclerViewAdapter_UpcomingEvents()
//        recyclerViewUpComingEvents.adapter = adapterUpcomingEvents


        //email.text="${mAuth.currentUser!!.email}"


        db.firestoreSettings = settings

        setUpRecyclerViewForUpcomingEvents()


//        layoutManager=LinearLayoutManager(this)
//        (layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
//        recyclerViewPastEvents.layoutManager=layoutManager
//        adapterPastEvents = RecyclerViewAdapter_PastEvents()
//        recyclerViewPastEvents.adapter=adapterPastEvents

        setUpRecyclerViewForPastEvents()


        layoutManager=LinearLayoutManager(this)
        (layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
        recyclerViewOrganizers.layoutManager=layoutManager

        adapterOrganizers = RecyclerViewAdapter_Organizers()
        recyclerViewOrganizers.adapter=adapterOrganizers


        val db = Firebase.firestore
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings

        show.setOnClickListener {
            db.collection("Upcoming Events").orderBy("Event Date").get().addOnSuccessListener{
                var x=0
                var size=it.documents.size
                var docs:String=""
                var titles:String=""
                while(x<=(size-1)) {
                    docs+= it.documents[x].data.toString()+"\n\n"
                    titles+=it.documents[x].data?.get("Event Title").toString()
                    x++
                }
                displayContents1.text=docs
                displayContents2.text=titles
            }
        }

        usersdisplay.setOnClickListener {
            val intent=Intent(this,UsersDisplayActivity::class.java)
            startActivity(intent)
        }

//        button.setOnClickListener{
//            signOut(mAuth)
//        }


//        del.setOnClickListener{
//            val user = Firebase.auth.currentUser!!
//            user.delete()
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful)
//                        {
//                            val db = Firebase.firestore
//                            val settings = firestoreSettings {
//                                isPersistenceEnabled = true
//                            }
//                            db.firestoreSettings = settings
//
//                            db.collection("Users").document(user.uid).delete().addOnSuccessListener {
//                                signOut(mAuth)
//                            }
//                        }
//                        else Toast.makeText(this,"Deletion Unsuccessful",Toast.LENGTH_SHORT).show()
//                    }
//        }
        //End of Delete Operation

    }

    private fun setUpRecyclerViewForPastEvents() {
        val query=db.collection("Past Events").orderBy("TimestampEvent")
        val firestoreRecyclerOptions:FirestoreRecyclerOptions<EventModel> = FirestoreRecyclerOptions.Builder<EventModel>().
        setQuery(query,EventModel::class.java).build()

        pastEventAdapter= EventAdapter(firestoreRecyclerOptions)

        layoutManager=LinearLayoutManager(this)
        (layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
        recyclerViewPastEvents.layoutManager=layoutManager
        recyclerViewPastEvents.adapter=pastEventAdapter
    }


    private fun setUpRecyclerViewForUpcomingEvents() {
        val query=db.collection("Upcoming Events").orderBy("TimestampEvent")
        val firestoreRecyclerOptions:FirestoreRecyclerOptions<EventModel> = FirestoreRecyclerOptions.Builder<EventModel>().
        setQuery(query,EventModel::class.java).build()

        upcomingEventAdapter= EventAdapter(firestoreRecyclerOptions)

        layoutManager=LinearLayoutManager(this)
        (layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
        recyclerViewUpComingEvents.layoutManager=layoutManager
        recyclerViewUpComingEvents.adapter=upcomingEventAdapter
    }


    override fun onStart() {
        super.onStart()
        upcomingEventAdapter!!.startListening()
        pastEventAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        upcomingEventAdapter!!.stopListening()
        pastEventAdapter!!.stopListening()
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.signOut->{
                signOut(mAuth)
            }
            R.id.del->{
                val user = Firebase.auth.currentUser!!
                user.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful)
                        {
                            val db = Firebase.firestore
                            val settings = firestoreSettings {
                                isPersistenceEnabled = true
                            }
                            db.firestoreSettings = settings

                            db.collection("Users").document(user.uid).delete().addOnSuccessListener {
                                signOut(mAuth)
                            }
                        }
                        else Toast.makeText(this,"Please Sign Out and Sign In Again to Delete",Toast.LENGTH_SHORT).show()
                    }
            }
            R.id.privacyPolicy->{
                val privacyIntent=Intent(this,PrivacyActivity::class.java)
                startActivity(privacyIntent)
            }
            R.id.addEvent->{
                val eventIntent=Intent(this,AddEventActivity::class.java)
                startActivity(eventIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun signOut(mAuth:FirebaseAuth)
    {
        Firebase.auth.signOut()
        mAuth.signOut()
        GoogleSignIn.getClient(this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut().addOnSuccessListener {
            val intent= Intent(this@DashboardActivity,SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}