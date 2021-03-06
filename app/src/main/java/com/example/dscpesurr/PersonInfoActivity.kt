package com.example.dscpesurr

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.text.isDigitsOnly
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_person_info.*
import kotlinx.android.synthetic.main.activity_sign_in_acitivity.*
import java.util.*

class PersonInfoActivity : AppCompatActivity()
{
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_info)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        //to change title of activity
        val actionBar = supportActionBar
        actionBar!!.hide()
//        actionBar!!.title = "Let us know you better"

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        //Cloud Firestore Things
        val db = Firebase.firestore
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings


        val studentDetails = hashMapOf(
                "Name" to "${user?.displayName}",
                "Email" to "${user?.email}",
                "Phone" to "${user?.phoneNumber}",
                "UID" to user?.uid,
                "filledAllDetails" to false
        )


        db.collection("Users").document(user!!.uid).set(studentDetails).addOnSuccessListener { documentReference ->
            Log.d("PersonalInfoActivity", "DocumentSnapshot successfully written! $documentReference")
        }
                .addOnFailureListener { e ->
                    Log.w("PersonalInfoActivity", "Error adding document", e)
                }


        greet2.text = "${user.displayName}"
        //image.setImageResource(user.photoUrl)
        Glide.with(this).load(user.photoUrl).into(image)


        var flagSRN=1
        var flagSrnValidate=1
        var flagBranch=1
        var flagSemester=1
        var flagCheck=1
        var filledAllDetails=false

        var sem=-1
        var branch=""


        var SRNString:String=SRN.text.toString()

        if(SRNString.isEmpty())flagSRN=1
        else
        {
            flagSrnValidate=SRNValidate(SRNString)
            flagSRN=0
        }

        val branchSpinner = findViewById<Spinner>(R.id.spinnerBranch)
        val adapter = BranchArrayAdapter(this,Branches.list!!)
        branchSpinner.adapter=adapter

        branchSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val selectedItem = parent!!.getItemAtPosition(position)
                val s=selectedItem.toString()
                branch=s.substring(s.lastIndexOf('=')+1)
                branch=branch.substring(0,branch.indexOf(')'))

                //Toast.makeText(this@PersonInfoActivity, "$branch", Toast.LENGTH_SHORT).show()
//                Toast.makeText(this@PersonInfoActivity,
//                        getString(R.string.selected_item) + " " +
//                                "" + semester[position], Toast.LENGTH_SHORT).show()
                    if(position==0)flagBranch=1
                    else{ flagBranch=0;
                        //branch=branches[position];
                    }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                flagBranch=1
            }




//        if (branchSpinner != null)
//        {
//            val semesterAdapter = ArrayAdapter(
//                    this,
//                    android.R.layout.simple_spinner_item, branches
//            )
//            semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//
//            branchSpinner.adapter = semesterAdapter
//
//            branchSpinner.onItemSelectedListener = object :
//                    AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(parent: AdapterView<*>,
//                                            view: View, position: Int, id: Long) {
////                    Toast.makeText(this@PersonInfoActivity,
////                        getString(R.string.selected_item) + " " +
////                                "" + semester[position], Toast.LENGTH_SHORT).show()
//                    if(position==0)flagBranch=1
//                    else{ flagBranch=0;branch=branches[position];}
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>) {
//                    flagBranch=1
//                }
//            }
//
       }

        val semester = resources.getStringArray(R.array.SemesterEven)
        val semesterSpinner = findViewById<Spinner>(R.id.spinnerSemester)

        if (semesterSpinner != null)
        {
            val semesterAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, semester
            )
            semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            semesterSpinner.adapter = semesterAdapter

            semesterSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
//                    Toast.makeText(this@PersonInfoActivity,
//                        getString(R.string.selected_item) + " " +
//                                "" + semester[position], Toast.LENGTH_SHORT).show()
                    if(position==0)flagSemester=1
                    else{ flagSemester=0;sem=semester[position].toInt();}
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    flagSemester=1
                }
            }
        }
        // End of Semester Spinner

        checkBox.setOnClickListener {
            if(checkBox.isChecked)flagCheck=0
            else flagCheck=1
        }


        privacy.setOnClickListener {
            val privacyIntent=Intent(this,PrivacyActivity::class.java)
            startActivity(privacyIntent)
        }


        agree.setOnClickListener {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (vibrator.hasVibrator()) { // Vibrator availability checking
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE)) // New vibrate method for API Level 26 or higher
                } else {
                    vibrator.vibrate(10) // Vibrate method for below API Level 26
                }
            }


            SRNString=SRN.text.toString()

            if(SRNString.isEmpty())flagSRN=1
            else {
                flagSRN = 0
                flagSrnValidate=SRNValidate(SRNString)
            }


            if(flagSRN==1){
                val snack = Snackbar.make(it,"Please Enter Your SRN or USN",Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(flagSrnValidate==1){
                val snack = Snackbar.make(it,"Please Enter Valid SRN or USN",Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(flagBranch==1){
                val snack = Snackbar.make(it,"Please Select Your Branch",Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(flagSemester==1){
                val snack = Snackbar.make(it,"Please Select Your Current Semester",Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(flagCheck==1){
                val snack = Snackbar.make(it,"Please Agree To The Terms",Snackbar.LENGTH_LONG)
                snack.show()
            }


            if(flagSemester==0 && flagSRN==0 && flagBranch==0 && flagCheck==0 && flagSrnValidate==0)
            {
                filledAllDetails=true
//                Toast.makeText(this,"SRN:${SRN}\nBranch:${branch}\nSemester:${sem}",Toast.LENGTH_LONG).show()
                val finalValidation=finalValidation(SRNString,branch,sem)
                if(finalValidation==0)
                {
                    val studentDetails = hashMapOf(
                            "Name" to "${user.displayName}",
                            "Email" to "${user.email}",
                            "Phone" to "${user.phoneNumber}",
                            "SRN" to SRNString,
                            "Branch" to branch,
                            "Semester" to sem,
                            "UID" to user.uid,
                            "filledAllDetails" to filledAllDetails
                    )


                    db.collection("Users").document(user.uid).update(studentDetails as Map<String, Any>).addOnSuccessListener { documentReference ->
                        Log.d("PersonalInfoActivity", "DocumentSnapshot successfully written! $documentReference")
                    }
                            .addOnFailureListener { e ->
                                Log.w("PersonalInfoActivity", "Error adding document", e)
                            }

//                    db.collection(SRNString).add(studentDetails).addOnSuccessListener { documentReference ->
//                        Log.d("PersonalInfoActivity", "DocumentSnapshot added with ID: ${documentReference.id}")
//                    }
//                            .addOnFailureListener { e ->
//                                Log.w("PersonalInfoActivity", "Error adding document", e)
//                            }

                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else
                {
                    val snack = Snackbar.make(it,"Details Don't Match | Please try again!",Snackbar.LENGTH_LONG)
                    snack.show()
                }
            }
        }
    }

    private fun finalValidation(srn: String, branch: String, sem: Int): Any
    {
        var result=1
        val SRNString=srn.trim().toUpperCase(Locale.ROOT)
        var isUSN=true
        if(SRNString.substring(6).isDigitsOnly())isUSN=false
        if(isUSN)
        {
            val year = SRNString.substring(6, 8)
            if(sem==2&&year=="20" || sem==4&&year=="19" || sem==6&&year=="18" || sem==8&&year=="17")
            {
                val br = SRNString.substring(8, 10)
                if(br=="CS"&&branch=="Computer Science" || br=="EC"&&branch=="Electronics & Communication" || br=="EE"&&branch=="Electrical & Electronics" || br=="ME"&&branch=="Mechanical" || br=="CV"&&branch=="Civil" || br=="BT"&&branch=="Biotechnology")
                    result=0
                else return 1
            }
            else return 1
        }
        else
        {
            val year = SRNString.substring(4, 8)
            if(sem==2&&year=="2020" || sem==4&&year=="2019" || sem==6&&year=="2018" || sem==8&&year=="2017")
            {
                result=0
            }
            else return 1
        }
        return result
    }

    private fun SRNValidate(srn:String): Int
    {
        var result=1
        var SRNString=srn.trim().toUpperCase(Locale.ROOT)

        if(SRNString.length!=13)return 1;
        else if(SRNString.length==13)
        {
            var isUSN=true
            if(SRNString.substring(6).isDigitsOnly())isUSN=false

//            Toast.makeText(this,"SRNString=${SRNString}\nisUSN=${isUSN}",Toast.LENGTH_LONG).show()

                if (SRNString.substring(0, 3) != "PES") return 1
                else
                {
                    if (SRNString[3] == '1' || SRNString[3] == '2')
                    {
                        if(isUSN)
                        {
                            if (SRNString[4] == 'U' && SRNString[5] == 'G') {
                                val year = SRNString.substring(6, 8)
                                if (year == "17" || year == "18" || year == "19" || year == "20") {
                                    val br = SRNString.substring(8, 10)
                                    if (br == "CS" || br == "EC" || br == "EE" || br == "ME" || br == "CV" || br == "BT")
                                    {
                                        if(SRNString.substring(10).isDigitsOnly())
                                        {result = 0}
                                        else return 1
                                    } else return 1
                                } else return 1
                            } else return 1
                        }
                        else
                        {
                            val year=SRNString.substring(4,8)
                            if(year=="2017" || year=="2018" || year=="2019" || year=="2020")
                            {
                                result=0
                            }
                            else return 1
                        }
                    }
                    else return 1
                }
        }
        else return 1
        return result
    }
}