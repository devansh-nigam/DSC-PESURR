package com.example.dscpesurr

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_event.*
import kotlinx.android.synthetic.main.activity_person_info.*
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.seconds


class AddEventActivity : AppCompatActivity()
{
    lateinit var datePicker: DatePickerHelper
    lateinit var timePicker: TimePickerHelper
    var filepath: Uri?=null
    var FirebaseDate:Long=0//= //Timestamp(Date(1, 1, 1))

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        image_preview.isVisible=false

        var flagDomain=1
        var flagTitle=1
        var flagImage=1
        var flagDetails=1
        var domain:String=""

        var eventTitleText:String
        var eventDetailsText:String

        dateDisplay.isVisible = false
        timeDisplay.isVisible = false

        eventDetails.setOnTouchListener(OnTouchListener { v, event ->
            if (eventDetails.hasFocus()) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_SCROLL -> {
                        v.parent.requestDisallowInterceptTouchEvent(false)
                        return@OnTouchListener true
                    }
                }
            }
            false
        })


        val domains = resources.getStringArray(R.array.Domain)
        val domainSpinner = findViewById<Spinner>(R.id.spinnerDomain)

        if (domainSpinner != null)
        {
            val semesterAdapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item, domains
            )
            semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            domainSpinner.adapter = semesterAdapter

            domainSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    if(position==0)flagDomain=1
                    else{ flagDomain=0;
                        domain = domains[position];}
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    flagDomain=1
                }
            }
        }

        datePicker = DatePickerHelper(this)
        timePicker = TimePickerHelper(this, false, false)

        //Cloud Firestore Things
        val db = Firebase.firestore
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings


        selectDate.setOnClickListener{
            showDatePickerDialog()
        }

        selectTime.setOnClickListener {
            showTimePickerDialog()
        }

        uploadImage.setOnClickListener {

            eventTitleText=eventTitle.text.toString()

            if(eventTitleText.isEmpty())flagTitle=1
            else {
                flagTitle = 0
            }

            if(flagTitle==1){
                val snack = Snackbar.make(it, "Please Enter Title Of Event Before Uploading Banner.", Snackbar.LENGTH_LONG)
                snack.show()
            }
            else{
                flagImage=0
                Toast.makeText(this, "Ready to Upload Image to Bucket", Toast.LENGTH_SHORT).show()
                var i= Intent()
                i.setType("image/*")
                i.setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(Intent.createChooser(i, "Choose Banner For Event"), 111)
            }
        }


        addEvent.setOnClickListener {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (vibrator.hasVibrator()) { // Vibrator availability checking
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE)) // New vibrate method for API Level 26 or higher
                } else {
                    vibrator.vibrate(10) // Vibrate method for below API Level 26
                }
            }

            eventTitleText=eventTitle.text.toString()

            if(eventTitleText.isEmpty())flagTitle=1
            else {
                flagTitle = 0
            }

            eventDetailsText=eventDetails.text.toString()

            if(eventDetailsText.isEmpty())flagDetails=1
            else {
                flagDetails = 0
            }


            if(flagTitle==1){
                val snack = Snackbar.make(it, "Please Enter Title Of Event.", Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(flagDetails==1){
                val snack = Snackbar.make(it, "Please Enter Details of Event.", Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(flagDomain==1){
                val snack = Snackbar.make(it, "Please Select The Domain Of Event.", Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(flagImage==1||filepath==null){
                val snack = Snackbar.make(it, "Please Upload An Event Banner in JPG/JPEG/PNG Format", Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(!dateDisplay.isVisible){
                val snack = Snackbar.make(it, "Please Select an Event Date", Snackbar.LENGTH_LONG)
                snack.show()
            }
            else if(!timeDisplay.isVisible){
                val snack = Snackbar.make(it, "Please Select an Event Time", Snackbar.LENGTH_LONG)
                snack.show()
            }
            else{
                    var pd=ProgressDialog(this)
                    pd.setTitle("Adding Event!")
                    pd.show()

                val date=dateDisplay.text.toString()
                val ar=date.split("/");
                val year=ar[2].toInt()
                val month=ar[1].toInt()
                val dayOfMonth=ar[0].toInt()

                val time=timeDisplay.text.toString()
                val ar2=time.split(":")
                var hour=ar2[0].toInt()
                if(hour>12)hour-=12
                val ar3=ar2[1].split(" ")
                val min=ar3[0].toInt()
                FirebaseDate = java.sql.Timestamp.UTC(year,month,dayOfMonth,hour,min,0)
                val cal = Calendar.getInstance()
                val d = cal.get(Calendar.DAY_OF_MONTH)
                var m = cal.get(Calendar.MONTH)
                m+=1
                val y = cal.get(Calendar.YEAR)
                val hourC=cal.get(Calendar.HOUR)
                val minC=cal.get(Calendar.MINUTE)
                val sec=cal.get(Calendar.SECOND)

//                                Toast.makeText(this,"Month=$m\nHourC=$hourC",Toast.LENGTH_SHORT).show()
                var CurrentDate = java.sql.Timestamp.UTC(y, m, d, hourC, minC, sec)

                var path=""
                if(CurrentDate<=FirebaseDate)
                {
                    path="Upcoming Events"
                }else{
                    path="Past Events"
                }


//                val selectedDate=dateDisplay.text.toString()
//                val sdf = SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH)
//                val formatedDate = sdf.parse(selectedDate)
//                val selectedTime = formatedDate!!.time / 60000


                    val imageRef= FirebaseStorage.getInstance().reference.child("Events/$path/" + eventTitleText + "/banner.jpg")
                    imageRef.putFile(filepath!!)
                            .addOnSuccessListener {

                                val event = hashMapOf(
                                        "EventTitle" to eventTitleText,
                                        "EventDetails" to eventDetailsText,
                                        "EventDomain" to domain,
                                        "EventDate" to "${dateDisplay.text}",
                                        "EventTime" to "${timeDisplay.text}",
                                        "TimestampEvent" to FirebaseDate,
                                        "TimestampNow" to CurrentDate,
                                        "EventBannerURL" to imageRef.path
                                )


                                db.collection(path).document(eventTitleText).set(event).addOnSuccessListener {
                                    pd.dismiss()
                                    Toast.makeText(this, "Event Added Successfully!", Toast.LENGTH_LONG).show()
                                    val intent=Intent(this, DashboardActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }.addOnFailureListener {
                                    pd.dismiss()
                                    Toast.makeText(this, "Event Cannot be Added! Please Try Again!", Toast.LENGTH_LONG).show()
                                }
                            }.addOnFailureListener {
                                pd.dismiss()
                                Toast.makeText(this, "Event Cannot be Added! Please Try Again!", Toast.LENGTH_LONG).show()
                            }.addOnProgressListener {
                                val progress=(100.00*it.bytesTransferred)/it.totalByteCount
                                pd.setMessage("Added ${progress.toInt()}%")
                            }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==111 && resultCode== Activity.RESULT_OK && data!=null)
        {
            filepath=data.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filepath)
            //bitmap.compress(Bitmap.CompressFormat.JPEG,25)
            image_preview.isVisible=true
            image_preview.setImageBitmap(bitmap)
        }
    }

    private fun showDatePickerDialog()
    {
        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)
        datePicker.showDialog(d, m, y, object : DatePickerHelper.Callback {
            override fun onDateSelected(dayofMonth: Int, month: Int, year: Int) {
                val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                val mon = month + 1
                val monthStr = if (mon < 10) "0${mon}" else "${mon}"
                dateDisplay.isVisible = true

                val selectedDate = "${dayStr}/${monthStr}/${year}"
                dateDisplay.text = selectedDate
            }
        })
    }

    private fun showTimePickerDialog()
    {
        val cal = Calendar.getInstance()
        val h = cal.get(Calendar.HOUR_OF_DAY)
        val m = cal.get(Calendar.MINUTE)
        timePicker.showDialog(h, m, object : TimePickerHelper.Callback {
            override fun onTimeSelected(hourOfDay: Int, minute: Int) {
                val hourStr = if (hourOfDay < 10) "0${hourOfDay}" else "${hourOfDay}"
                val minuteStr = if (minute < 10) "0${minute}" else "${minute}"
                timeDisplay.isVisible = true
                timeDisplay.text = "${hourOfDay}:${minuteStr} hrs"
            }
        })
    }
}