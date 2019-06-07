package com.example.thesis_pepper

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val ip = "130.251.13.194"
    private val port = 8080
    private val client = ClientSocket(ip, port)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Requirements check for estimote
        RequirementsWizardFactory.createEstimoteRequirementsWizard().fulfillRequirements(
            this,
            onRequirementsFulfilled = {
                Log.d("Beacons","onRequirementsFulfilled")

                // start proximity service
                val proximityServiceIntent = Intent(this,ProximityService::class.java)
                ContextCompat.startForegroundService(this,proximityServiceIntent)

            },
            onRequirementsMissing = {},
            onError = {}
        )

        // use button to test communication with server
        bt_startclient.setOnClickListener {
                client.openClient()
                //message from phone
                tv_phone.text = client.receive
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //if implement the onStartCommand() callback method, must explicitly stop the service,
        val proximityServiceIntent = Intent(this,ProximityService::class.java)
        stopService(proximityServiceIntent)
        Log.d("main","proximityDestory")
        //val intent = Intent(this, TryServerService::class.java)
        //stopService(intent)


    }

}
