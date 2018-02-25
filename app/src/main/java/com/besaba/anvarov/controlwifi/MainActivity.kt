package com.besaba.anvarov.controlwifi

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Math.*
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    private lateinit var router: Location
    private lateinit var sPref: SharedPreferences
    private val savedLatitude = "saved_latitude"
    private val savedLongitude = "saved_longitude"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermission()
        val client: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getRouter.setOnClickListener {
            ActivityCompat.checkSelfPermission(this@MainActivity, ACCESS_FINE_LOCATION)
            client.lastLocation.addOnSuccessListener(this@MainActivity) { location ->
                if (location != null) {
                    router = location
                    txLocation.text = "широта  ${location.latitude.toString()}\u00B0 \n" +
                            "долгота ${location.longitude.toString()}\u00B0"
                }
            }
        }

        saveRouter.setOnClickListener {
            saveText()
        }

        loadText()
    }

    private fun loadText() {
        sPref = getPreferences(Context.MODE_PRIVATE)
        txLocation.text = "широта  ${sPref.getFloat(savedLatitude, 0.0F).toString()}\u00B0 \n" +
                "долгота ${sPref.getFloat(savedLongitude, 0.0F).toString()}\u00B0"
    }

    private fun saveText() {
        sPref = getPreferences(Context.MODE_PRIVATE)
        val ed = sPref.edit()
        ed.putFloat(savedLatitude, router.latitude.toFloat())
        ed.putFloat(savedLongitude, router.longitude.toFloat())
        ed.apply()
    }

    fun distanceAB(latA: Double, longA: Double, latB: Double, longB: Double) =
            (round(atan2(sqrt((cos(PI * latB / 180) * sin(PI * longB / 180 - PI * longA / 180)).pow(2) +
                    (cos(PI * latA / 180) * sin(PI * latB / 180) - (sin(PI * latA / 180) * cos(PI * latB / 180) *
                            cos(abs(PI * longB / 180 - PI * longA / 180)))).pow(2)), sin(PI * latA / 180) *
                    sin(PI * latB / 180) + cos(PI * latA / 180) * cos(PI * latB / 180) * cos(abs(PI * longB / 180 -
                    PI * longA / 180))) * 6372795).toInt()).toString()


    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), 1)
    }
}

