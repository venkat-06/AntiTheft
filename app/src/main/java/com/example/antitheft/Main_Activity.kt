package com.example.antitheft

import android.app.AlertDialog

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.net.Uri
import android.os.BatteryManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log

import android.view.Menu
import android.view.MenuItem

import android.view.WindowManager
import android.widget.ImageView

import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import android.widget.Toast

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Main_Activity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SensorEventListener {
    private lateinit var motionSwitch: Switch
    private lateinit var proximitySwitch: Switch
    private lateinit var chargerSwitch: Switch
    private lateinit var navMenuImageProfile: ImageView
    private lateinit var navMenuNameText: TextView
    private lateinit var cdt: CountDownTimer
    private lateinit var sensorMan: SensorManager
    private lateinit var mSensorManager: SensorManager
    private lateinit var mSensor: Sensor
    private lateinit var accelerometer: Sensor
    private lateinit var mGravity: FloatArray
    private var mAccel: Float = 0.toFloat()
    private val handler = Handler(Looper.getMainLooper())
    private val delay: Long = 2000 // 2 seconds
    private var mAccelCurrent: Float = 0.toFloat()
    private var mAccelLast: Float = 0.toFloat()
    private lateinit var alertDialog: AlertDialog
    private val SENSOR_SENSITIVITY = 4
    private var mSwitchSet: Int = 0
    private var pSwitchSet: Int = 0
    private var chargerFlag: Int = 0
    private var chargerFlag1: Int = 0
    private var drawer: DrawerLayout? = null
    private var chargerFlag2: Int = 0

    override fun onResume() {
        super.onResume()
        sensorMan.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorMan.unregisterListener(this)
        mSensorManager.unregisterListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val nvDrawer: NavigationView = findViewById(R.id.nav_view)
        setupDrawerContent(nvDrawer)
        val headerView = nvDrawer.getHeaderView(0)
        navMenuImageProfile = headerView.findViewById(R.id.imageView)
        val imageUriString = intent.getStringExtra("imageUri")
        val imageUri:Uri? = imageUriString?.let { Uri.parse(it) }
        imageUri?.let {
        Glide.with(this).load(it).into(navMenuImageProfile)
        } ?: run {

             Glide.with(this).load(R.drawable.userimage_placeholder).into(navMenuImageProfile)
        }
        navMenuNameText = headerView.findViewById(R.id.textViewemail)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)!!
        sensorMan = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        alertDialog = AlertDialog.Builder(this).create()
        loadUserName()
        chargerSwitch = findViewById(R.id.sCharger) as Switch
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)

                if (plugged == BatteryManager.BATTERY_PLUGGED_AC) {
                    chargerFlag = 1
                } else if (plugged == 0) {
                    chargerFlag1 = 1
                    chargerFlag = 0
                    func()
                }
            }
        }
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(receiver, filter)

        chargerSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (chargerFlag != 1) {
                    Toast.makeText(this@Main_Activity, "Connect To Charger", Toast.LENGTH_SHORT).show()
                    chargerSwitch.isChecked = false
                } else {
                    Toast.makeText(this@Main_Activity, "Charger Protection Mode On", Toast.LENGTH_SHORT).show()
                    chargerFlag2 = 1
                    func()
                }
            } else {
                chargerFlag2 = 0
            }
        }

        mAccel = 0.00f
        mAccelCurrent = SensorManager.GRAVITY_EARTH
        mAccelLast = SensorManager.GRAVITY_EARTH
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        motionSwitch = findViewById(R.id.sMotion) as Switch
        motionSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                alertDialog.setTitle("Will Be Activated In 10 Seconds")
                alertDialog.setMessage("00:10")

                cdt = object : CountDownTimer(10000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        alertDialog.setMessage("00:" + millisUntilFinished / 1000)
                    }

                    override fun onFinish() {
                        mSwitchSet = 1
                        alertDialog.hide()
                        Toast.makeText(this@Main_Activity, "Motion Detection Mode Activated", Toast.LENGTH_SHORT).show()
                    }
                }.start()
                alertDialog.show()
                alertDialog.setCancelable(false)
            } else {
                Toast.makeText(this@Main_Activity, "Motion Switch Off", Toast.LENGTH_SHORT).show()
                mSwitchSet = 0
            }
        }
        proximitySwitch = findViewById(R.id.sProximity) as Switch
        proximitySwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                alertDialog.setTitle("Keep Phone In Your Pocket")
                alertDialog.setMessage("00:10")

                cdt = object : CountDownTimer(10000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        alertDialog.setMessage("00:" + millisUntilFinished / 1000)
                    }

                    override fun onFinish() {
                        pSwitchSet = 1
                        alertDialog.hide()
                    }
                }.start()
                alertDialog.show()
                alertDialog.setCancelable(false)
            } else {
                Toast.makeText(this@Main_Activity, "Motion Switch Off", Toast.LENGTH_SHORT).show()
                pSwitchSet = 0
            }
        }



        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener{menuItem ->
            selectDrawerItem(menuItem)
            true
        }
    }
    private fun loadUserName() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val userId = user.uid
            val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userName = dataSnapshot.child("userName").getValue(String::class.java)
                    val usermail = dataSnapshot.child("userNumber").getValue(String::class.java)
                    // Set the user name to the TextView
                    navMenuNameText.text = " $userName \n $usermail"
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors
                    Log.e(ContentValues.TAG, "Failed to load user name: ${databaseError.message}")
                }
            })
        }
    }
    private val runnable = object : Runnable {
        override fun run() {
            // Your function to be executed every 2 seconds
            loadUserName()

            // Schedule the task to run again after 2 seconds
            handler.postDelayed(this, delay)
        }
    }



    fun func() {
        if (chargerFlag == 0 && chargerFlag1 == 1 && chargerFlag2 == 1) {
            startActivity(Intent(this@Main_Activity, EnterPin::class.java))
            chargerFlag2 = 0
            finish()
        }
    }

    override fun onBackPressed() {
        drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    fun selectDrawerItem(menuItem: MenuItem)
    {
        when(menuItem.itemId){
            R.id.nav_profile -> {
                val i = Intent(this, User_profile::class.java)
                startActivity(i)
            }
            R.id.nav_feed ->{
                val i = Intent(this,feedback ::class.java)
                startActivity(i)

            }
            R.id.nav_web ->{
                val i = Intent(this,webView ::class.java)
                startActivity(i)

            }
            R.id.nav_logout->{
                showLogoutConfirmationDialog()
            }
            R.id.nav_loc->{
                val i = Intent(this, location ::class.java)
                startActivity(i)
            }
        }
        drawer?.closeDrawers()
    }

    private fun showLogoutConfirmationDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { dialog, which ->
                // Logout user
                FirebaseAuth.getInstance().signOut()
                // Redirect to login or splash screen
                // For example:
                val intent = Intent(this, Login_activity::class.java)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_settings) {
            startActivity(Intent(this@Main_Activity, ResetPin::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

       /* if (id == R.id.nav_camera) {
        } else if (id == R.id.nav_gallery) {
        } else if (id == R.id.nav_slideshow) {
        } else if (id == R.id.nav_manage) {
        } else if (id == R.id.nav_share) {

        }*/

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
    fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            selectDrawerItem(menuItem)
            true
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            mGravity = event.values.clone()
            val x = mGravity[0]
            val y = mGravity[1]
            val z = mGravity[2]
            mAccelLast = mAccelCurrent
            mAccelCurrent = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta = mAccelCurrent - mAccelLast
            mAccel = mAccel * 0.9f + delta
            if (mAccel > 1.0) {
                if (mSwitchSet == 1) {
                    startActivity(Intent(this@Main_Activity, EnterPin::class.java))
                    finish()
                }
            }
        } else if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
            } else if (pSwitchSet == 1) {
                startActivity(Intent(this@Main_Activity, EnterPin::class.java))
                finish()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }
}


